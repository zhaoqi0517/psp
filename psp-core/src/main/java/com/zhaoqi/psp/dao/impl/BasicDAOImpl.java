package com.zhaoqi.psp.dao.impl;

import com.zhaoqi.psp.dao.BasicDAO;
import com.zhaoqi.psp.domain.Id;
import com.zhaoqi.psp.util.BeanUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qi on 17-11-20.
 */
public class BasicDAOImpl extends SqlSessionDaoSupport implements BasicDAO {

    static class SqlInfo {
        public String tableName;
        public String findAll;
        public String deleteById;
        public String insert;
        public String update;
        public Map<String, String> updateMap;
    }

    private SqlInfo getSqlInfo(Class<?> clazz) {

        SqlInfo result = caches.get(clazz);
        if (result == null) {
            result = createSqlInfo(clazz);
            caches.put(clazz, result);
        }
        return result;
    }
    private SqlInfo createSqlInfo(Class<?> clazz) {

        SqlInfo si = new SqlInfo();
        String className = clazz.getSimpleName();
        String tableName = toDBString(className);

        si.tableName = tableName;
        si.findAll = "select * from " + tableName;
        si.deleteById = "delete from " + tableName + idCondition;

        boolean isIdData = clazz.isAssignableFrom(Id.class);
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(clazz);
        List<String> cols = new ArrayList<>();
        List<String> vals = new ArrayList<>();
        Map<String, String> updateMap = new HashMap<>();

        for (PropertyDescriptor prop : props) {

            if (prop.getReadMethod() == null) {
                continue;
            }

            String name = prop.getName();
            if ("class".equals(name) || ("id".equals(name) && isIdData)) {
                continue;
            }
            String col = toDBString(name);
            String val = toValueString(name);
            cols.add(col);
            vals.add(val);
            updateMap.put(name, col + " = " + val);
        }

        si.insert = "insert into "
                + tableName + " (" + StringUtils.collectionToCommaDelimitedString(cols) + ") "
                + "values (" + StringUtils.collectionToCommaDelimitedString(vals) + ")";

        si.update = "update " + tableName + " set "
                + StringUtils.collectionToCommaDelimitedString(updateMap.values()) + idCondition;
        si.updateMap = updateMap;

        return si;
    }

    @Override
    public int insert(Object object) {
        String sql = getSqlInfo(object.getClass()).insert;
        return getSqlSession().insert(sql, object);
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        return null;
    }

    @Override
    public int update(Id object) {
        String sql = getSqlInfo(object.getClass()).update;
        sql += getUpdateAt(object);
        log.debug(sql);
        return getSqlSession().update(sql, object);
    }

    @Override
    public int updateNotNull(Id object) {

        SqlInfo si = getSqlInfo(object.getClass());
        String sql = "update " + si.tableName + " set ";

        List<String> updates = new ArrayList<>();
        for (Map.Entry<String, String> e : si.updateMap.entrySet()) {
            if (BeanUtil.getProperty(object, e.getKey()) != null) {
                updates.add(e.getValue());
            }
        }

        sql += StringUtils.collectionToCommaDelimitedString(updates);
        sql = sql + idCondition + getUpdateAt(object);
        log.debug(sql);
        return getSqlSession().update(sql, object);
    }

    @Override
    public int deleteById(Id object) {
        String sql = getSqlInfo(object.getClass()).deleteById;
        return getSqlSession().delete(sql, object);
    }

    private String getUpdateAt(Object obj) {
        if (BeanUtil.getProperty(obj, "updateAt") == null) {
            return "";
        }
        return " and updateAt = #{updateAt}";
    }
    /**
     * 追加下划线。注意，大小写没有整理。
     * @param str
     * @return
     */
    private String toDBString(String str) {
        boolean hasUnderScore = getSqlSession().getConfiguration().isMapUnderscoreToCamelCase();
        return hasUnderScore ? str.replaceAll("([a-z])([A-Z])", "$1_$2") : str;
    }

    /**
     * 追加MyBatis格式。
     * @param str
     * @return
     */
    private String toValueString(String str) {
        return "#{" + str + "}";
    }

    private static final Map<Class<?>, SqlInfo> caches = new HashMap<>();
    private static final String idCondition = " where id=#{id}";
    private static final Logger log = LoggerFactory.getLogger(BasicDAOImpl.class);

}
