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
package org.springframework.social.wechat.api.impl.json;

import java.io.IOException;
import java.util.List;

import org.springframework.social.wechat.api.WechatProfile.Sex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Annotated mixin to add Jackson annotations to WechatProfile. 
 * @author Craig Walls
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class WechatProfileMixin extends WechatObjectMixin {

	@JsonProperty("openid")
	String openId;
	
	@JsonProperty("nickname")
	String nickName;
	
	@JsonProperty("sex")
	@JsonDeserialize(using=SexDeserializer.class)
	Sex sex;
	
	@JsonProperty("province")
	String province;
	
	@JsonProperty("city")
	String city;
	
	@JsonProperty("country")
	String country;
	
	@JsonProperty("headimgurl")
	String headImgUrl;
	
	@JsonProperty("privilege")
	List<String> privilege;
	
	@JsonProperty("unionid")
	String unionId;
	
	private static class SexDeserializer extends JsonDeserializer<Sex> {
		@Override
		public Sex deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			try {
				return Sex.valueOf(jp.getIntValue());
			} catch (IllegalArgumentException e) {
				return Sex.UNKNOWN;
			}				
		}		
	}
	
}
