package com.zhaoqi.psp.aop;

import java.util.Properties;

import javax.annotation.Resource;

import com.zhaoqi.psp.dao.Handler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * Created by qi on 17-11-20.
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = { MappedStatement.class, Object.class }),
        @Signature(
                type = Executor.class,
                method = "query",
                args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class MybatisInterceptor implements Interceptor {

        @Resource
        protected Handler daoHandler;

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            return daoHandler.handle(invocation);
        }

        @Override
        public Object plugin(Object target) {
            return Plugin.wrap(target, this);
        }

        @Override
        public void setProperties(Properties properties) {
        }


}
