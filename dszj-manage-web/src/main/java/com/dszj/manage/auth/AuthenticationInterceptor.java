package com.dszj.manage.auth;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.dszj.SystemProperties;
import com.dszj.manage.base.ResultEnum;
import com.dszj.manage.base.ResultVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限拦截器定义
 * 
 * @author yys
 */
@Slf4j
@SuppressWarnings({ "rawtypes" })
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	@Autowired
	private SystemProperties systemProperties;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		Object accountInfo = request.getSession().getAttribute(SessionKeyEnum.ACCOUNT.getCode());
		// 判断接口是否权限控制
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		PublicAuth publicAuth = method.getAnnotation(PublicAuth.class);
		String url = request.getRequestURI().replace(request.getContextPath(), "");
		
		// 已经登录的情况
		if (accountInfo != null) {
			// 后台用户权限验证
			if (accountInfo instanceof UserInfo) {
				UserInfo userInfo = (UserInfo) accountInfo;
				request.setAttribute(SessionKeyEnum.ACCOUNT.getCode(), userInfo);
				//关闭权限验证
				if (!systemProperties.isOpenAuth()) {
					return true;
				}
				
				if (publicAuth == null) {
					List<String> urls = (List<String>) request.getSession().getAttribute(SessionKeyEnum.AUTH.getCode());
					if (!urls.contains(url)) {
						ResultVO resultVO = new ResultVO<>();
						resultVO.setCode(ResultEnum.PERMISSION_DENIED.getCode());
						resultVO.setErrDesc("权限不足");
						Map<String, Object> logMap = new HashMap<>();
						logMap.put("url", url);
						logMap.put("userInfo", userInfo);
						logMap.put("publicAuth", publicAuth);
						log.error("权限不足拦截日志-->{}", logMap);
						response.setCharacterEncoding("UTF-8");
						response.setStatus(403);
						response.setContentType("application/json; charset=utf-8");
						try {
							response.getWriter().append(JSONObject.toJSONString(resultVO));
						} catch (Exception e) {
							log.error("权限不足，处理异常", e);
						}
						return false;
					}
				}
			}

			// 前台用户权限验证
			if (accountInfo instanceof MemberInfo) {
				MemberInfo memberInfo = (MemberInfo) accountInfo;
				request.setAttribute(SessionKeyEnum.ACCOUNT.getCode(), memberInfo);
				List<String> urls = (List<String>) request.getSession().getAttribute(SessionKeyEnum.AUTH.getCode());

			}

			// 未登录的情况
		} else {
			//需要权限验证
			if (publicAuth==null) {
				request.getRequestDispatcher("/login/toLogin").forward(request, response);
				return false;
			}
		}

		return true;

	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}
}
