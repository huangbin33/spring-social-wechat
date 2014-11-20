/**
 * 
 */
package org.springframework.social.wechat.connect;

import org.springframework.social.oauth2.AccessGrant;

/**
 * @author jhcao
 *
 */
@SuppressWarnings("serial")
public class WechatAccessGrant extends AccessGrant {

	private String openId;
	
	public WechatAccessGrant(String accessToken) {
		super(accessToken);
	}
	
	public WechatAccessGrant(String accessToken, String scope, String refreshToken,
			Long expiresIn) {
		super(accessToken, scope, refreshToken, expiresIn);
	}
	
	public WechatAccessGrant(String accessToken, String scope, String refreshToken,
			Long expiresIn, String openId) {
		super(accessToken, scope, refreshToken, expiresIn);
		this.openId = openId;
	}

	public String getOpenId() {
		return openId;
	}	


}
