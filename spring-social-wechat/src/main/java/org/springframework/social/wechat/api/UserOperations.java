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

import org.springframework.social.ApiException;

public interface UserOperations {
	
	/**
	 * Retrieves the profile for the specified user.
	 * @param openId the Wechat user ID to retrieve profile data for.
	 * @return the user's profile information.
	 * @throws ApiException if there is an error while communicating with Wechat.
	 */
	WechatProfile getUserProfile(String openId);
	
	public static final String BASE_URI = "https://api.weixin.qq.com/sns/userinfo";
	
	public static final String PARAM_NAME_OPEN_ID = "openid";
	
}
