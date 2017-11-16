package com.zhaoqi.psp.util;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;
import static org.springframework.web.util.WebUtils.ERROR_EXCEPTION_ATTRIBUTE;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zhaoqi.psp.config.AppConfig;
import com.zhaoqi.psp.domain.User;
import com.zhaoqi.psp.exception.BaseException;
import com.zhaoqi.psp.exception.SystemException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

public class WebUtil {

	public static final String KEY_USER = "user";

	public static Locale getLocale() {
		return RequestContextUtils.getLocale(getRequestAttributes().getRequest());
	}

	/**
	 * 保存异常。
	 * @param exception
	 * @param isRedirect
	 */
	public static void saveError(Object exception, boolean isRedirect) {
		int scope = isRedirect ? SCOPE_SESSION : SCOPE_REQUEST;
		getRequestAttributes().setAttribute(ERROR_EXCEPTION_ATTRIBUTE, exception, scope);
	}

	public static List<ObjectError> getErrors() {

		Exception exception = (Exception) getAttribute(ERROR_EXCEPTION_ATTRIBUTE);
		if (exception == null) {
			return null;
		}

		if (exception instanceof BindException) {
			return ((BindException) exception).getAllErrors();
		}

		if (!(exception instanceof BaseException)) {
			exception = new SystemException(exception);
		}

		ObjectError error = ((BaseException) exception).getError();
		return Arrays.asList(new ObjectError[] { error });
	}

	public static void invalidate() {
		HttpSession session = getSession();
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * Jsp名前でUrlを取得する。
	 * @param view
	 * @return
	 */
	public static String getJspUrl(String view) {

		if (StringUtils.isEmpty(view)) {
			return view;
		}

		String slash = AppConfig.SLASH;
		if (view.startsWith(slash)) {
			view = view.substring(slash.length());
		}

		String prefix = AppConfig.get("jspPrefix");
		String suffix = AppConfig.get("jspSuffix");
		return prefix + view + suffix;
	}


	public static boolean isJson(HttpServletRequest request) {
		return request.getRequestURI().endsWith("." + AppConfig.get("jsonSuffix"));
	}

	public static boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	public static User getUser() {
		HttpSession session = getSession();
		return session == null ? null : (User) session.getAttribute(KEY_USER);
	}

	public static void setUser(User user) {
		HttpSession session = getSession();
		if (session != null) {
			session.setAttribute(KEY_USER, user);
		}
	}

	/**
	 * 如果以redirect:开头，则判断为redirect。
	 * @param path
	 * @return
	 */
	public static boolean isRedirect(String path) {
		return REDIRECT_URL_PREFIX.equals(getPrefix(path));
	}

	/**
	 * forward:或redirect:の存在を判別する。
	 * @param viewName
	 * @return true：有り
	 */
	public static boolean hasPrefix(String viewName) {
		return !StringUtils.isEmpty(getPrefix(viewName));
	}

	/**
	 * 取forward:或redirect:
	 * @param viewName
	 * @return
	 */
	public static String getPrefix(String viewName) {

		if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
			return REDIRECT_URL_PREFIX;
		}

		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			return FORWARD_URL_PREFIX;
		}

		return "";
	}


	public static Object getAttribute(String key) {

		Object value = (Exception) getRequestAttributes().getAttribute(key, SCOPE_REQUEST);
		if (value != null) {
			return value;
		}

		return getRequestAttributes().getAttribute(key, SCOPE_SESSION);
	}

	static HttpSession getSession() {
		return getRequestAttributes().getRequest().getSession(false);
	}

	static ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	}

	private WebUtil() {
	}
}
