package com.zhaoqi.psp.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhaoqi.psp.action.Layout;
import com.zhaoqi.psp.util.ActionUtil;
import com.zhaoqi.psp.util.WebUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class ViewInterceptor extends HandlerInterceptorAdapter {

	private String defaultLayout;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		if (!HandlerMethod.class.isInstance(handler)) {
			return true;
		}

		HandlerMethod method = (HandlerMethod) handler;
		ActionUtil.saveCurrentInput(method);
		ActionUtil.savePackageUrl(method);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView view) throws Exception {

		if (view == null) {
			return;
		}

		String result = view.getViewName();
		if (result == null || hasExt(result) || !HandlerMethod.class.isInstance(handler)) {
			// 有扩展名时都会走共通比如json或xls，所以不处理。
			return;
		}

		HandlerMethod method = (HandlerMethod) handler;

		result = ActionUtil.addPackageUrl(method, result);
		view.setViewName(result);

		// Layout処理。
		processLayout(method, view);
	}

	public void setDefaultLayout(String defaultLayout) {
		this.defaultLayout = defaultLayout;
	}

	/**
	 */
	private void processLayout(HandlerMethod method, ModelAndView view) {

		String name = view.getViewName();
		if (WebUtil.hasPrefix(name)) {
			return;
		}

		String layoutName = defaultLayout;
		Layout annotation = ActionUtil.getLayout(method);
		if (annotation != null) {
			if (annotation.noNeed()) {
				return;
			}
			layoutName = annotation.view();
		}

		name = WebUtil.getJspUrl(name);
		view.addObject(subviewName, name);
		view.setViewName(layoutName);
	}


	private boolean hasExt(String view) {
		return view.indexOf('.') >= 0;
	}

	private final static String subviewName = "subview";
}
