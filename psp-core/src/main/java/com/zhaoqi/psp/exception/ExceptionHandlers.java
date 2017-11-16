package com.zhaoqi.psp.exception;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhaoqi.psp.util.ActionUtil;
import com.zhaoqi.psp.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;


/**
 * 全体例外処理。
 * <ul>
 * <li>値チェック例外とサービス例外の場合、メッセージを表示する。
 * <li>システム例外とそのたの例外の場合、システムエラー画面へ遷移する。
 * </ul>
 * @author clxy
 */
@ControllerAdvice
public class ExceptionHandlers {

	@Resource
	private MappingJackson2HttpMessageConverter jackson2Converter;

	/**
	 * <ul>
	 * <li>値チェック例外。
	 * <li>サービス例外。
	 * </ul>
	 * @param exception
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler({ BindException.class, ServiceException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleService(Exception exception,
			HttpServletRequest request, HttpServletResponse response) {

		if (WebUtil.isJson(request)) {
			write(exception, response);
			return null;
		}

		if (WebUtil.isAjax(request)) {
			return "common/messages";
		}

		if (exception instanceof ServiceException) {
			String view = ((ServiceException) exception).getView();
			if (view != null) {
				return view;
			}
		}

		String input = ActionUtil.getCurrentInput();
		WebUtil.saveError(exception, WebUtil.isRedirect(input));
		return input;
	}

	/**
	 * システムエラーとその他の例外を処理する。
	 * @param exception
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler({ SystemException.class, Exception.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleSystem(Exception exception,
			HttpServletRequest request, HttpServletResponse response) {

		SystemException result = null;
		if (exception instanceof SystemException) {
			result = (SystemException) exception;
		} else {
			result = new SystemException(exception);
		}

		log.error("システム例外発生：", exception);

		if (WebUtil.isAjax(request)) {
			write(result, response);
			return null;
		}

		return new ModelAndView(
				"redirect:/" + result.getView(),
				WebUtils.ERROR_EXCEPTION_ATTRIBUTE, result);
	}

	/**
	 * 例外をJSONで出力する。
	 * @param exception
	 * @param response
	 */
	private void write(Exception exception, HttpServletResponse response) {

		try {
			// エラーだと明記。
			ServletServerHttpResponse sshr = new ServletServerHttpResponse(response);
			sshr.setStatusCode(HttpStatus.BAD_REQUEST);

			// 情報を抽出。
			Map<String, Object> result = new HashMap<>();
			if (exception instanceof BaseException) {
				BaseException be = (BaseException) exception;
				result.put(viewKey, be.getView());
				result.put(errorKey, be.getError());
			} else {
				result.put(errorKey, exception.getLocalizedMessage());
			}

			// 出力。
			jackson2Converter.write(result, MediaType.APPLICATION_JSON, sshr);

		} catch (Exception e) {
			// Exception処理しているから何もできないじゃん！
			log.debug("ExceptionHandlers#write", e);
		}
	}

	private static final String viewKey = "view";
	private static final String errorKey = "error";
	private static final Logger log = LoggerFactory.getLogger(ExceptionHandlers.class);
}
