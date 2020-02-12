package com.dszj.manage.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dszj.manage.auth.LoginAccount;
import com.dszj.manage.auth.PublicAuth;
import com.dszj.manage.auth.SessionKeyEnum;
import com.dszj.manage.auth.UserInfo;
import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Menu;
import com.dszj.manage.entity.User;
import com.dszj.manage.enums.MenuTypeEnum;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.service.MenuService;
import com.dszj.manage.service.RoleService;
import com.dszj.manage.service.UserService;
import com.dszj.manage.utils.BeanCopyUtil;
import com.dszj.manage.utils.ValidateCodeUtil;

/**
 * 登录控制器
 * @author yys
 */
@Controller
@RequestMapping("/login")
@SuppressWarnings("rawtypes")
public class LoginController extends BaseController {
	

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
	
    @PublicAuth
    @GetMapping(value = "/toLogin")
	public String toLogin() {
		return "back/login";
	}
	
    @PublicAuth
	@PostMapping(value = "/login")
	@ResponseBody
	public ResultVO login(String username, String password, String code,HttpServletRequest request) {
    	
    	if (!((String)request.getSession().getAttribute(SessionKeyEnum.LOGIN_CODE.getCode())).equalsIgnoreCase(code)) {
    		throw new BizException(BizExceptionEnum.PARAM_ERROR,"验证码错误");
		}
    	
    	
		User user = userService.findByUsernameAndPassword(username, DigestUtils.md5Hex(password.trim()));
		if (user != null) {
			UserInfo userInfo = BeanCopyUtil.convertBean(user, UserInfo.class);
			userInfo.setRoles(roleService.findByUserId(user.getId()));
			request.getSession().setAttribute(SessionKeyEnum.ACCOUNT.getCode(), userInfo);
		}else{
			throw new BizException(BizExceptionEnum.PARAM_ERROR,"用户名或密码错误");
		}
		
		List<Menu> menus = null;
		//管理员账户
		if (user.getId()==1) {
			menus = menuService.findAll();
		}else{
			menus = menuService.findByUserId(user.getId());
		}
		Map<String,Menu> labelMap = new HashMap<>();
		List<String> urls = new ArrayList<>();
		for (Menu menu : menus) {
			//菜单资源
			if (MenuTypeEnum.OPERATE.getCode().equals(menu.getType())) {
				labelMap.put(menu.getLabel(), menu);
			}
			//缓存权限资源
			if (!StringUtils.isEmpty(menu.getUrl())) {
				urls.add(menu.getUrl());
			}
			
		}
		request.getSession().setAttribute(SessionKeyEnum.AUTH.getCode(), urls);
		
		return success(labelMap);
	}
    
    @PublicAuth
    @GetMapping(value = "/logout")
    @ResponseBody
    public ResultVO logout(@LoginAccount UserInfo userInfo,HttpServletRequest requst) {
    	if (userInfo!=null) {
    		requst.getSession().removeAttribute(SessionKeyEnum.ACCOUNT.getCode());
    		requst.getSession().removeAttribute(SessionKeyEnum.AUTH.getCode());
		}
    	return success();
    }
    

	/**
	 * 获取图片验证码
	 *
	 * @return
	 * @author yys
	 * @throws IOException
	 */
	@GetMapping(value = "/getValidateCode")
	@PublicAuth
	public void getValidateCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		// 生成文字验证码
		String code = ValidateCodeUtil.createCode();
		request.getSession().setAttribute(SessionKeyEnum.LOGIN_CODE.getCode(), code);
		// 生成图片验证码
		BufferedImage image = ValidateCodeUtil.createImage(code);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		out.flush();

	}
	
}
