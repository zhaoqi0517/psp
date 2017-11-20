package com.zhaoqi.psp.dao;

public interface Dialect {
	String getLimitString(String sql, int offset, int limit);
}
