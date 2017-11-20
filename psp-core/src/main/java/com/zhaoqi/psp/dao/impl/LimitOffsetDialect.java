package com.zhaoqi.psp.dao.impl;

import com.zhaoqi.psp.dao.Dialect;

public class LimitOffsetDialect implements Dialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return sql + " limit " + offset + ", " + limit;
	}
}
