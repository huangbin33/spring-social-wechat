/**
 * 
 */
package org.springframework.social.wechat.api;

import java.io.Serializable;

/**
 * Model class containing a Wechat OK json information : {"errcode":0,"errmsg":"ok"}.
 * @author jhcao
 *
 */
@SuppressWarnings("serial")
public class WechatOkMessage extends WechatObject implements Serializable {

	private String errorCode;
	private String errorMessage;
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
