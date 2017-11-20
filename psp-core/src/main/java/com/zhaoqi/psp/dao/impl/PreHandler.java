package com.zhaoqi.psp.dao.impl;

import java.util.Date;

import com.zhaoqi.psp.dao.Handler;
import com.zhaoqi.psp.domain.BasicEntity;
import com.zhaoqi.psp.domain.User;
import com.zhaoqi.psp.util.WebUtil;
import org.apache.ibatis.plugin.Invocation;


public class PreHandler implements Handler {

	@Override
	public Object handle(Invocation invocation) throws Throwable {

		Object argParam = HandlerUtil.getParameter(invocation);
		if (!(argParam instanceof BasicEntity) || HandlerUtil.isNotNullMethod(invocation)) {
			return null;
		}

		BasicEntity data = (BasicEntity) argParam;
		Date now = new Date();
		if (data.getCreateAt() == null) {
			data.setCreateAt(now);
		}
		if (data.getUpdateAt() == null) {
			data.setUpdateAt(now);// TODO 做版本控制时该如何？
		}

		User user = WebUtil.getUser();
		if (user == null) {
			return null;
		}
		if (data.getCreateBy() == null) {
			data.setCreateBy(user.getId());
		}
		if (data.getUpdateBy() == null) {
			data.setUpdateBy(user.getId());
		}

		return null;
	}
}