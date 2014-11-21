/**
 * 
 */
package org.springframework.social.wechat.api.impl.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Annotated mixin to add Jackson annotations to WechatOkMessage. 
 * @author jhcao
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class WechatOkMessageMixin extends WechatObjectMixin {

	@JsonProperty("errcode")
	String errorCode;
	
	@JsonProperty("errmsg")
	String errorMessage;
	
}
