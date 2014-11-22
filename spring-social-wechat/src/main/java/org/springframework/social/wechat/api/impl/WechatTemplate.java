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


import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.social.wechat.api.UserOperations;
import org.springframework.social.wechat.api.ValidationOperations;
import org.springframework.social.wechat.api.Wechat;
import org.springframework.social.wechat.api.impl.json.WechatModule;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>This is the central class for interacting with Wechat.</p>
 * <p>
 * There are some operations, such as searching, that do not require OAuth
 * authentication. In those cases, you may use a {@link WechatTemplate} that is
 * created through the default constructor and without any OAuth details.
 * Attempts to perform secured operations through such an instance, however,
 * will result in {@link NotAuthorizedException} being thrown.
 * </p>
 * @author Craig Walls
 */
public class WechatTemplate extends AbstractOAuth2ApiBinding implements Wechat {
	
	private UserOperations userOperations;
	
	private ValidationOperations validationOperations;

	private ObjectMapper objectMapper;

	/**
	 * Create a new instance of WechatTemplate.
	 * This constructor creates the WechatTemplate using a given access token.
	 * Wechat TokenStrategy should use ACCESS_TOKEN_PARAMETER, which add parameter 'access_token=ACCESS_TOKEN' to wechat requests.
	 * @param accessToken An access token given by Wechat after a successful OAuth 2 authentication (or through Wechat's JS library).
	 */	
	public WechatTemplate(String accessToken) {
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
		initialize();
	}
	
	@Override
	public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(requestFactory));
	}

	public UserOperations userOperations() {
		return userOperations;
	}
	
	public ValidationOperations validationOperations() {
		return validationOperations;
	}
	
	public RestOperations restOperations() {
		return getRestTemplate();
	}

	@Override
	protected void configureRestTemplate(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new WechatErrorHandler());
	}

	@Override
	protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
		MappingJackson2HttpMessageConverter converter = super.getJsonMessageConverter();
		objectMapper = new ObjectMapper();				
		objectMapper.registerModule(new WechatModule());
		converter.setObjectMapper(objectMapper);		
		return converter;
	}
	
	// private helpers
	private void initialize() {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
		initSubApis();
	}
		
	private void initSubApis() {
		userOperations = new UserTemplate(restOperations(), isAuthorized());
		validationOperations = new ValidationTemplate(restOperations(), isAuthorized());
	}
	
}
