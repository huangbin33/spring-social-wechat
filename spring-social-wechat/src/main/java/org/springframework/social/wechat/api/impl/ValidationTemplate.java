/**
 * 
 */
package org.springframework.social.wechat.api.impl;

import org.springframework.social.wechat.api.ValidationOperations;
import org.springframework.social.wechat.api.WechatOkMessage;
import org.springframework.web.client.RestOperations;

/**
 * @author jhcao
 *
 */
public class ValidationTemplate extends AbstractWechatOperations implements
		ValidationOperations {

	public ValidationTemplate(RestOperations restOperations,
			boolean isAuthorized) {
		super(restOperations, isAuthorized);
	}

	/* (non-Javadoc)
	 * @see org.springframework.social.wechat.api.ValidationOperations#validateAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validateAccessToken(String accessToken, String openId) {
		requireAuthorization();
		WechatOkMessage error = 
			fetchObject(BASE_URI, WechatOkMessage.class, PARAM_NAME_OPEN_ID, openId);
		if(error!=null && error.getErrorMessage().equals("ok")){
			return true;
		}
		return false;
	}

}
