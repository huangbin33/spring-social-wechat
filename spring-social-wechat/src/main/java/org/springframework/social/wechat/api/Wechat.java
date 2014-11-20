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
package org.springframework.social.wechat.api;

import org.springframework.social.ApiBinding;
import org.springframework.social.wechat.api.impl.WechatTemplate;
import org.springframework.web.client.RestOperations;


/**
 * Interface specifying a basic set of operations for interacting with Wechat.
 * Implemented by {@link WechatTemplate}.
 * @author Craig Walls
 */
public interface Wechat extends ApiBinding {
		
	/**
	 * API for performing operations on Wechat user profiles.
	 * @return {@link UserOperations}
	 */
	UserOperations userOperations();
	
	/**
	 * Returns the underlying {@link RestOperations} object allowing for consumption of Wechat endpoints that may not be otherwise covered by the API binding.
	 * The RestOperations object returned is configured to include an OAuth 2 "Authorization" header on all requests.
	 * @return RestOperations instrumented to include Authorization header on all requests
	 */
	RestOperations restOperations();
	

}
