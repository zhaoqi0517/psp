package com.zhaoqi.psp.listener;

import com.zhaoqi.psp.domain.User;
import com.zhaoqi.psp.util.WebUtil;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by qi on 17-11-20.
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute(WebUtil.KEY_USER, new User());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
