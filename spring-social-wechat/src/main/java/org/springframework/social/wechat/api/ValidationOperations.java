/**
 * 
 */
package org.springframework.social.wechat.api;

/**
 * Auth Operations
 * 
 * @author jhcao
 *
 */
public interface ValidationOperations {

	/**
	 * check if the access_token for the user's openid is valid.
	 * 
	 * @param accessToken access_token
	 * @param openId user's openid
	 * @return true if the accessToken for the openId is valid; false otherwise.
	 */
	public boolean validateAccessToken(String accessToken, String openId);
	
	public static final String BASE_URI = "https://api.weixin.qq.com/sns/auth";
	public static final String PARAM_NAME_OPEN_ID = "openid";
	public static final String PARAM_NAME_ACCESS_TOKEN = "access_token";
}
