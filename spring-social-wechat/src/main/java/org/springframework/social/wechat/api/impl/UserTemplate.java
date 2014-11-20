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

import org.springframework.social.wechat.api.UserOperations;
import org.springframework.social.wechat.api.WechatProfile;
import org.springframework.web.client.RestOperations;

public class UserTemplate extends AbstractWechatOperations implements UserOperations {

	public UserTemplate(RestOperations restOperations, boolean isAuthorizedForUser) {
		super(restOperations, isAuthorizedForUser);
	}

	@Override
	public WechatProfile getUserProfile(String openId) {
		requireAuthorization();
		return fetchObject(BASE_URI, WechatProfile.class, PARAM_NAME_OPEN_ID, openId);
		
	}
	
}
