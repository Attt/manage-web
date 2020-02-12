package com.dszj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 基础服务配置类
 * @author yys
 */
@Data
@Component
public class SystemProperties {
	
	//文件上传大小限制配置
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;
	@Value("${spring.servlet.multipart.max-request-size}")
	private String maxRequestSize;	
    
    //开启权限验证
	@Value("${system.open.auth}")
	private boolean openAuth;

	@Value("${weChat.web.appId}")
	private String weChatWebAppId;
	@Value("${weChat.web.appSecret}")
	private String weChatWebAppSecret;
	@Value("${weChat.web.token}")
	private String weChatWebToken; //所有公众号共用token

}
