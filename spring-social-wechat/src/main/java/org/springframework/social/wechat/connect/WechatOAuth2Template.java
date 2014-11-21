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

import java.util.Map;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template2;
import org.springframework.util.MultiValueMap;

/**
 * Wechat-specific extension of OAuth2Template2 to use a RestTemplate that recognizes form-encoded responses as "text/plain".
 * Wechat token responses are form-encoded results with a content type of "text/plain", which prevents the FormHttpMessageConverter
 * registered by default from parsing the results.
 * @author John Cao
 */
public class WechatOAuth2Template extends OAuth2Template2 {

	protected String PARAM_NAME_CLIENT_ID = "appid";		//overridable
	protected String PARAM_NAME_CLIENT_SECRET = "secret";	//overridable
	
	public WechatOAuth2Template(String clientId, String clientSecret) {
		super(clientId, clientSecret, 
			"https://open.weixin.qq.com/connect/qrconnect", 
			null,
			"https://api.weixin.qq.com/sns/oauth2/access_token",
			"https://api.weixin.qq.com/sns/oauth2/refresh_token"
		);
		setUseParametersForClientAuthentication(true);

	}
	
	@Override
	protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
		if ("client_credentials".equals(parameters.getFirst("grant_type"))) {
			parameters.set("appid", parameters.getFirst("client_id"));
			parameters.set("secret", parameters.getFirst("client_secret"));
			parameters.set("grant_type", parameters.getFirst("client_credential"));
			parameters.remove("client_id");
			parameters.remove("client_secret");
		}
		return super.postForAccessGrant(accessTokenUrl, parameters);
	}
	
	@Override
	protected AccessGrant createAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn, Map<String, Object> response) {
		return new WechatAccessGrant(accessToken, scope, refreshToken, expiresIn, (String)response.get("openid"));	
	}
		
}
