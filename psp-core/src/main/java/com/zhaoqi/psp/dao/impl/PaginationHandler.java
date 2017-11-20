package com.zhaoqi.psp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import com.zhaoqi.psp.dao.BasicDAO;
import com.zhaoqi.psp.dao.DAOUtil;
import com.zhaoqi.psp.dao.Dialect;
import com.zhaoqi.psp.dao.Handler;
import com.zhaoqi.psp.services.impl.PaginationCondition;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 自动分页拦截器。
 * <ul>
 * <li>是否分页取决于查询参数是否是PaginationCondition类。</li>
 * <li>先查询总件数，如果总件数为0，不再继续查询下去。</li>
 * <li>完全无视MyBatis的RowBounds。不可并用！</li>
 * </ul>
 * 参考自https://github.com/yfyang/mybatis-pagination
 * @author clxy
 */
public class PaginationHandler implements Handler {

	private Dialect dialect;

	@Override
	public Object handle(Invocation invocation) throws Throwable {

		Object argParam = HandlerUtil.getParameter(invocation);
		if (!(argParam instanceof PaginationCondition)) {
			// 如果参数不是分页条件，不处理。
			return null;
		}

		MappedStatement statement = HandlerUtil.getStatement(invocation);
		BoundSql originalBound = statement.getBoundSql(argParam);
		String originalSql = originalBound.getSql().trim();

		// 件数：select count。
		String countSql = "select count(1) from (" + originalSql + ") as auto_count";
		log.debug(countSql);
		int count = getCount(statement, originalBound, countSql);
		if (count == 0) {
			// 结果为空时不再继续查询。
			return Collections.EMPTY_LIST;
		}
		PaginationCondition condition = (PaginationCondition) argParam;
		condition.setCount(count);

		// 分页：set limit and offset。
		String pageSql =
				dialect.getLimitString(originalSql, condition.getOffset(), condition.getLimit());
		log.debug(pageSql);
		invocation.getArgs()[0] = DAOUtil.createBy(statement, originalBound, pageSql);

		return invocation.proceed();
	}

	/**
	 * 查询总纪录数。
	 * @param statement
	 * @param originalBound
	 * @param countSql
	 * @return
	 * @throws java.sql.SQLException
	 */
	private static int getCount(MappedStatement statement, BoundSql originalBound, String countSql)
			throws SQLException {

		Object param = originalBound.getParameterObject();
		BoundSql countBound = new BoundSql(
				statement.getConfiguration(), countSql, originalBound.getParameterMappings(), param);

		Connection conn =
				statement.getConfiguration().getEnvironment().getDataSource().getConnection();

		try (PreparedStatement ps = conn.prepareStatement(countSql)) {

            DAOUtil.setParameters(ps, statement, countBound, param);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			return count;
		}
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	private static final Logger log = LoggerFactory.getLogger(PaginationHandler.class);
}
