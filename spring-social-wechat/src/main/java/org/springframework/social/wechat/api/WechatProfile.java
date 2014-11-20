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

import java.io.Serializable;
import java.util.List;

/**
 * Model class containing a Wechat user's profile information.
 * @author Craig Walls
 */
@SuppressWarnings("serial")
public class WechatProfile extends WechatObject implements Serializable {

	private String openId;
	private String nickName;
	private Sex sex;
	private String province;
	private String city;
	private String country;
	private String headImgUrl;
	private List<String> privilege;
	private String unionId;
		
//	WechatProfile() {}
//
//	public WechatProfile(String openId, String nickName, Integer sex,
//			String province, String city, String country, String headImgUrl,
//			List<String> privilege, String unionId) {
//		super();
//		this.openId = openId;
//		this.nickName = nickName;
//		this.sex = sex;
//		this.province = province;
//		this.city = city;
//		this.country = country;
//		this.headImgUrl = headImgUrl;
//		this.privilege = privilege;
//		this.unionId = unionId;
//	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public List<String> getPrivilege() {
		return privilege;
	}

	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	
	public static enum Sex { 
		UNKNOWN(0), MALE(1), FEMALE(2);
		
		private final int sexDigit;
		
		Sex(int sexDigit){
			this.sexDigit = sexDigit;
		}
		
		public int getSexDigit() {
			return sexDigit;
		}
		
		public static Sex valueOf(int sexDigit){
			for(Sex s: Sex.values()){
				if(s.getSexDigit()==sexDigit)
					return s;
			}
			return UNKNOWN;
		}
		
	}
	
	
}
