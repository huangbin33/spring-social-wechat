/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.wechat.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.RevokedAuthorizationException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Subclass of {@link DefaultResponseErrorHandler} that handles errors from Wechat's
 * Graph API, interpreting them into appropriate exceptions.
 * @author Craig Walls
 */
class WechatErrorHandler extends DefaultResponseErrorHandler {

	private final static Log logger = LogFactory.getLog(WechatErrorHandler.class);
	private final static String WECHAT = "wechat";

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		Map<String, String> errorDetails = extractErrorDetailsFromResponse(response);
		if (errorDetails == null) {
			handleUncategorizedError(response, errorDetails);
		}
		handleWechatError(response.getStatusCode(), errorDetails);
		
		// if not otherwise handled, do default handling and wrap with UncategorizedApiException
		handleUncategorizedError(response, errorDetails);			
	}

	/**
	 * Examines the error data returned from Wechat and throws the most applicable exception.
	 * @param errorDetails a Map containing a "type" and a "message" corresponding to the Graph API's error response structure.
	 */
	void handleWechatError(HttpStatus statusCode, Map<String, String> errorDetails) {
		// Can't trust the type to be useful. It's often OAuthException, even for things not OAuth-related. 
		// Can rely only on the message (which itself isn't very consistent).
		String message = errorDetails.get("message");
		if (statusCode == HttpStatus.NOT_FOUND) {
			if (message.contains("Some of the aliases you requested do not exist")) {
				throw new ResourceNotFoundException(WECHAT, message);
			}
		} else if (statusCode == HttpStatus.BAD_REQUEST) {
			if (message.contains("Unknown path components")) {
				throw new ResourceNotFoundException(WECHAT, message);
			} else if (message.equals("An access token is required to request this resource.")) {
				throw new MissingAuthorizationException(WECHAT);
			} else if (message.equals("An active access token must be used to query information about the current user.")) {
				throw new MissingAuthorizationException(WECHAT);
			} else if (message.startsWith("Error validating access token")) {
				handleInvalidAccessToken(message);
			} else if (message.equals("Invalid access token signature.")) { // Access token that fails signature validation
				throw new InvalidAuthorizationException(WECHAT, message);
			} else if (message.contains("Application does not have the capability to make this API call.") || message.contains("App must be on whitelist")) {
				throw new OperationNotPermittedException(WECHAT, message);
			} else if (message.contains("Invalid fbid") || message.contains("The parameter url is required")) { 
				throw new OperationNotPermittedException(WECHAT, "Invalid object for this operation");
			} else if (message.contains("Duplicate status message") ) {
				throw new DuplicateStatusException(WECHAT, message);
			} else if (message.contains("Feed action request limit reached")) {
				throw new RateLimitExceededException(WECHAT);
			} else if (message.contains("The status you are trying to publish is a duplicate of, or too similar to, one that we recently posted to Twitter")) {
				throw new DuplicateStatusException(WECHAT, message);
			}
		} else if (statusCode == HttpStatus.UNAUTHORIZED) {
			if (message.startsWith("Error validating access token")) {
				handleInvalidAccessToken(message);
			} else if (message.equals("Invalid OAuth access token.")) {  // Bogus access token
				throw new InvalidAuthorizationException(WECHAT, message);
			} else if (message.startsWith("Error validating application.")) { // Access token with incorrect app ID
				throw new InvalidAuthorizationException(WECHAT, message);
			}
			throw new NotAuthorizedException(WECHAT, message);
		} else if (statusCode == HttpStatus.FORBIDDEN) {
			if (message.contains("Requires extended permission")) {
				throw new InsufficientPermissionException(WECHAT, message.split(": ")[1]);
			} else if (message.contains("Permissions error")) {
				throw new InsufficientPermissionException(WECHAT);
			} else if (message.contains("The user hasn't authorized the application to perform this action")) {
				throw new InsufficientPermissionException(WECHAT);
			} else {
				throw new OperationNotPermittedException(WECHAT, message);
			}
		} else if (statusCode == HttpStatus.NOT_FOUND) {
			throw new ResourceNotFoundException(WECHAT, message);
		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
			//TODO: CASE ANALYSIS...
			throw new InternalServerErrorException(WECHAT, message);
			
		}
	}

	private void handleInvalidAccessToken(String message) {
		if (message.contains("Session has expired at unix time")) {
			throw new ExpiredAuthorizationException("wechat");
		} else if (message.contains("The session has been invalidated because the user has changed the password.")) {
			throw new RevokedAuthorizationException("wechat", message);
		} else if (message.contains("The session is invalid because the user logged out.")) {
			throw new RevokedAuthorizationException("wechat", message);
		} else if (message.contains("The session was invalidated explicitly using an API call.")) {
			throw new RevokedAuthorizationException("wechat", message);
		} else if (message.contains("Session does not match current stored session.")) {
			throw new RevokedAuthorizationException("wechat", message);
		} else {
			throw new InvalidAuthorizationException("wechat", message);
		}
	}

	private void handleUncategorizedError(ClientHttpResponse response, Map<String, String> errorDetails) {
		try {
			super.handleError(response);
		} catch (Exception e) {
			if (errorDetails != null) {
				throw new UncategorizedApiException("wechat", errorDetails.get("message"), e);
			} else {
				throw new UncategorizedApiException("wechat", "No error details from Wechat", e);
			}
		}
	}

	/*
	 * Attempts to extract Wechat error details from the response.
	 * Returns null if the response doesn't match the expected JSON error response.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> extractErrorDetailsFromResponse(ClientHttpResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new JsonFactory());
		String json = readFully(response.getBody());
		
		System.out.println(json);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Error from Wechat: " + json);
		}
		if (json.equals("false")) {
			// Sometimes FB returns "false" when requesting an object that the access token doesn't have permission for.
			throw new InsufficientPermissionException("wechat");
		}
				
		try {
		    Map<String, Object> responseMap = mapper.<Map<String, Object>>readValue(json, new TypeReference<Map<String, Object>>() {});
		    if (responseMap.containsKey("error")) {
		    	return (Map<String, String>) responseMap.get("error");
		    }
		} catch (JsonParseException e) {
			return null;
		}
	    return null;
	}
	
	private String readFully(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		while (reader.ready()) {
			sb.append(reader.readLine());
		}
		return sb.toString();
	}
}
