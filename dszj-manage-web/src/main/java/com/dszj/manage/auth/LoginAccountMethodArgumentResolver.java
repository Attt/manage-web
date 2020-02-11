package com.dszj.manage.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
@Component
public class LoginAccountMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(LoginAccount.class)) {
			if (parameter.getParameterType().isAssignableFrom(UserInfo.class)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
//		if (parameter.getParameterType().isAssignableFrom(BaseAccountInfo.class)) {
		if (parameter.getParameterType().getGenericSuperclass().getTypeName().equals(BaseAccountInfo.class.getName())) {
			Object account = webRequest.getAttribute(SessionKeyEnum.ACCOUNT.getCode(),RequestAttributes.SCOPE_REQUEST);
			if (account instanceof UserInfo) {
				if (account != null) {
					return (UserInfo)account;
				}
			}
			if (account instanceof MemberInfo) {
				if (account != null) {
					return (MemberInfo)account;
				}
			}
		
		}
		return null;
	}
}
