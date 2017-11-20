package com.zhaoqi.psp.dao;

import org.apache.ibatis.plugin.Invocation;

public interface Handler {

    Object handle(Invocation invocation) throws Throwable;
}
