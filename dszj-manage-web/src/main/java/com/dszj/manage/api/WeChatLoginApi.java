package com.dszj.manage.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dszj.SystemProperties;
import com.dszj.manage.dto.WeChatAccessTokenDTO;
import com.dszj.manage.dto.WeChatUserInfoDTO;
import com.dszj.manage.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 微信登录相关接口 api地址：
 * 网站应用微信登录：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=
 * resource/res_list&verify=1&id=open1419316505&token=
 * 1c2de08e4234812477adad5eaeb753ebb61ab88e&lang=zh_CN
 * （当前使用）基于公众号微信网页授权登录：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=
 * mp1421140842
 * 
 * @author yys
 */
@Component
@Slf4j
public class WeChatLoginApi {
	@Autowired
	private SystemProperties systemProperties;
	
	/**
	 * 通过前台授权code获取token信息
	 * @param code
	 * @author yys
	 */
	private WeChatAccessTokenDTO getAccessToken(String code) {
		WeChatAccessTokenDTO accessTokenDTO = new WeChatAccessTokenDTO();
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		String result = HttpClientUtil.doGet(
					url.replace("APPID", systemProperties.getWeChatWebAppId()).replace("SECRET", systemProperties.getWeChatWebAppSecret()).replace("CODE", code));


		log.info("WeChatLoginApi.getAccessToken-->{}", result);

		JSONObject json = JSON.parseObject(result.trim());
		// 失败返回信息
		if (json.containsKey("errcode")) {
			accessTokenDTO.setSuccess(false);
			accessTokenDTO.setErrCode(json.getString("errcode"));
			accessTokenDTO.setErrMsg(json.getString("errmsg"));
			return accessTokenDTO;
		}
		// 成功返回信息
		accessTokenDTO.setAccessToken(json.getString("access_token"));
		accessTokenDTO.setOpenId(json.getString("openid"));
		accessTokenDTO.setRefreshToken(json.getString("refresh_token"));
		accessTokenDTO.setExpiresIn(json.getString("expires_in"));
		accessTokenDTO.setScope(json.getString("scope"));
		return accessTokenDTO;
	}


	/**
	 * 获取用户个人信息
	 */
	private WeChatUserInfoDTO getUserInfo(String accessToken, String openId) {

		WeChatUserInfoDTO userInfoDTO = new WeChatUserInfoDTO();
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
		String result = HttpClientUtil.doGet(url.replace("APPID", systemProperties.getWeChatWebAppId()).replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId));

		
		log.info("WeChatLoginApi.getUserInfo-->{}", result);

		JSONObject json = JSON.parseObject(result.trim());
		// 失败返回信息
		if (json.containsKey("errcode")) {
			userInfoDTO.setSuccess(false);
			userInfoDTO.setErrCode(json.getString("errcode"));
			userInfoDTO.setErrMsg(json.getString("errmsg"));
			return userInfoDTO;
		}
		// //成功返回信息
		userInfoDTO.setNickname(json.getString("nickname"));
		userInfoDTO.setSex(json.getString("sex"));
		userInfoDTO.setProvince(json.getString("province"));
		userInfoDTO.setCity(json.getString("city"));
		userInfoDTO.setCountry(json.getString("country"));
		userInfoDTO.setPrivilege(json.getString("privilege"));
		userInfoDTO.setHeadImgUrl(json.getString("headimgurl"));
		userInfoDTO.setOpenId(json.getString("openid"));
		userInfoDTO.setUnionId(json.getString("unionid"));
		return userInfoDTO;
	}

	/**
	 * 获取用户个人信息
	 */
	public WeChatUserInfoDTO getUserInfo(String code) {

		WeChatUserInfoDTO userInfoDTO = new WeChatUserInfoDTO();
		// 获取accessToken信息
		WeChatAccessTokenDTO weChatAccessTokenDTO = this.getAccessToken(code);
		if (weChatAccessTokenDTO.isSuccess()) {
			//获取用户信息
			return this.getUserInfo(weChatAccessTokenDTO.getAccessToken(), weChatAccessTokenDTO.getOpenId());
		} else {
			userInfoDTO.setSuccess(false);
			userInfoDTO.setErrCode(weChatAccessTokenDTO.getErrCode());
			userInfoDTO.setErrMsg(weChatAccessTokenDTO.getErrMsg());
		}
		return userInfoDTO;
	}
}
