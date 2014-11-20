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

import java.net.URI;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

public class AbstractWechatOperations {
	
	private RestOperations restOperations;
	private final boolean isAuthorized;

	public AbstractWechatOperations(RestOperations restOperations, boolean isAuthorized) {
		this.restOperations = restOperations;
		this.isAuthorized = isAuthorized;
	}
	
	protected void requireAuthorization() {
		if (!isAuthorized) {
			throw new MissingAuthorizationException("facebook");
		}
	}
	
	public <T> T fetchObject(String BASE_URI, Class<T> type, MultiValueMap<String, String> queryParameters) {
		URI uri = URIBuilder.fromUri(BASE_URI).queryParams(queryParameters).build();
		return getRestOperations().getForObject(uri, type);
	}
	
	public <T> T fetchObject(String BASE_URI, Class<T> type, String paramName, String paramValue) {
		URI uri = URIBuilder.fromUri(BASE_URI).queryParam(paramName, paramValue).build();
		return getRestOperations().getForObject(uri, type);
	}

	protected RestOperations getRestOperations() {
		return restOperations;
	}
		
}
