package com.zhaoqi.psp.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.zhaoqi.psp.util.ActionUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/**
 * <ul>
 * <li>如果类中含带有RequestMapping的annotation的方法时，判定为Action类。<br>
 * 所以理论上任意类都可能成为Action。
 * <li>寻找package是否有RequestMapping的annotation，如果存在则合并。
 * </ul>
 * @author clxy
 */
public class ActionHandler extends RequestMappingHandlerMapping {

	@Override
	protected boolean isHandler(Class<?> beanType) {

		boolean result = super.isHandler(beanType);
		if (result) {
			return result;
		}

		// 包含带有RequestMapping的annotation的方法时，判定为Action类。
		for (Method m : beanType.getMethods()) {
			if (AnnotationUtils.findAnnotation(m, RequestMapping.class) != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		if (info == null) {
			return null;
		}

		// 登録する。
		ActionUtil.register(method);

		// 查找package是否有RequestMapping的annotation。
		Package p = handlerType.getPackage();
		RequestMappingInfo pInfo = packageMappings.get(p);
		if (pInfo == null && !packageMappings.containsKey(p)) {
			pInfo = createRequestMappingInfo(p);
			packageMappings.put(p, pInfo);
		}

		if (pInfo != null) {
			// 如果有则合并。
			info = pInfo.combine(info);
		}

		return info;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#createRequestMappingInfo
	 * @param p
	 * @return
	 */
	private RequestMappingInfo createRequestMappingInfo(Package p) {

		RequestMapping annotation = ActionUtil.getRequestMapping(p);
		if (annotation == null) {
			return null;
		}

		boolean useSuffixPatternMatch = false;// package的annotation无需对应各种pattern。
		boolean useTrailingSlashMatch = true;

		String[] patterns = ActionUtil.getRequestMappingValue(p);
		return new RequestMappingInfo(new PatternsRequestCondition(patterns, getUrlPathHelper(),
				getPathMatcher(), useSuffixPatternMatch, useTrailingSlashMatch, null),
				new RequestMethodsRequestCondition(annotation.method()),
				new ParamsRequestCondition(annotation.params()), new HeadersRequestCondition(
						annotation.headers()), new ConsumesRequestCondition(annotation.consumes(),
						annotation.headers()), new ProducesRequestCondition(annotation.produces(),
						annotation.headers(), getContentNegotiationManager()), null);
	}

	private static final Map<Package, RequestMappingInfo> packageMappings = new HashMap<>();
}
