package com.dszj.manage.utils;

import java.awt.image.BufferedImage;
import java.util.Properties;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码生成工具
 * https://blog.csdn.net/qq_33624284/article/details/79000767
 * @author yys
 *
 */
public class ValidateCodeUtil {
	
	public static Properties properties = new Properties();
	
	static {
	    properties.setProperty("kaptcha.image.width", "120");
	    properties.setProperty("kaptcha.image.height", "50");
	    properties.setProperty("kaptcha.textproducer.char.string", "23456789ABCDEFGHJKLMNPQRSTUVWXYZ");
	    properties.setProperty("kaptcha.textproducer.char.length", "4");
	    
	    properties.setProperty("kaptcha.border", "no");
//	    properties.setProperty("kaptcha.border.color", "105,179,90");
//	    properties.setProperty("kaptcha.border.thickness", "1");
	    properties.setProperty("kaptcha.textproducer.font.color", "black");
//	    properties.setProperty("kaptcha.textproducer.font.size", "25");
//	    properties.setProperty("kaptcha.textproducer.font.names", "楷体");
	    properties.setProperty("kaptcha.noise.color", "black");
//	    properties.setProperty("kaptcha.textproducer.char.space", "8");
//	    properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
	}
	
	/**
	 * 创建验证码图片
	 * @param code
	 * @return
	 * @author yys
	 */
	public static BufferedImage createImage(String code) {
	    
	    DefaultKaptcha defaultKaptcha = new  DefaultKaptcha();
	    defaultKaptcha.setConfig(new Config(properties));
	    //生成图片验证码
	    return defaultKaptcha.createImage(code);
	}
	
	/**
	 * 创建验证码
	 * @return
	 * @author yys
	 */
	public static String createCode() {
		DefaultKaptcha defaultKaptcha = new  DefaultKaptcha();
		defaultKaptcha.setConfig(new Config(properties));
		//生成文字验证码
		return defaultKaptcha.createText();
	}
	

}
