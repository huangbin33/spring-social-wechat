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
package org.springframework.social.wechat.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.wechat.api.Wechat;

/**
 * Wechat ApiAdapter implementation.
 * @author John Cao
 */
public class WechatAdapter implements ApiAdapter<Wechat> {

	public boolean test(Wechat wechat) {
		try {
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	public void setConnectionValues(Wechat wechat, ConnectionValues values) {
		throw new UnsupportedOperationException();
	}

	public UserProfile fetchUserProfile(Wechat wechat) {
		throw new UnsupportedOperationException();
	}
	
	public void updateStatus(Wechat wechat, String message) {
		throw new UnsupportedOperationException();
	}

}
