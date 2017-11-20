package com.zhaoqi.psp.dao.impl;

import com.zhaoqi.psp.dao.Handler;
import org.apache.ibatis.plugin.Invocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qi on 17-11-20.
 */
public class Handlers implements Handler {

    private List<Handler> handlers = new ArrayList<>();

    @Override
    public Object handle(Invocation invocation) throws Throwable {

        for (Handler handler : handlers) {
            Object result = handler.handle(invocation);
            if (result != null) {
                return result;
            }
        }

        return invocation.proceed();
    }

    public void setHandlers(List<Handler> handlers) {
        this.handlers = handlers;
    }
}
