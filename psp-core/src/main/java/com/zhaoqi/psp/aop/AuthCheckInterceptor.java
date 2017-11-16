package com.zhaoqi.psp.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhaoqi.psp.action.NoAuth;
import com.zhaoqi.psp.domain.User;
import com.zhaoqi.psp.util.AppUtil;
import com.zhaoqi.psp.util.WebUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class AuthCheckInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		User user = WebUtil.getUser();
		if (user != null && user.isLogined()) {
			return true;
		}

		HandlerMethod method = (HandlerMethod) handler;
		if (AppUtil.find(method, NoAuth.class) != null) {
			return true;
		}

		boolean isAjax = WebUtil.isAjax(request);
		if (isAjax) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			response.sendRedirect(request.getContextPath() + "/auth/");
		}

		return false;
	}
}
