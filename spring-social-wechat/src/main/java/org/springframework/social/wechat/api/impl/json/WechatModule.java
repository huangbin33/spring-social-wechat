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

import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.social.wechat.api.WechatOkMessage;
import org.springframework.social.wechat.api.WechatProfile;

/**
 * Jackson module for setting up mixin annotations on Wechat model types. 
 * This enables the use of Jackson annotations without
 * directly annotating the model classes themselves.
 * 
 * @author John Cao
 */
@SuppressWarnings("serial")
public class WechatModule extends SimpleModule {

	public WechatModule() {
		super("WechatModule");
	}
	
	@Override
	public void setupModule(SetupContext context) {		
		context.setMixInAnnotations(WechatProfile.class, WechatProfileMixin.class);	
		context.setMixInAnnotations(WechatOkMessage.class, WechatOkMessageMixin.class);	
	}
}
