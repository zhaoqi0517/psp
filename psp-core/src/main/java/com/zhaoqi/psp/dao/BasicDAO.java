package com.zhaoqi.psp.dao;

import com.zhaoqi.psp.domain.Id;

import java.util.List;

/**
 * Created by qi on 17-11-20.
 */
public interface BasicDAO {

    /**
     * Insert。注意，Null值也会被insert。
     * @param object
     * @return
     */
    public int insert(Object object);

    /**
     * 无条件选择所有。
     * @param clazz
     * @return
     */
    public <T> List<T> findAll(Class<T> clazz);

    /**
     * Update。条件是id和update时间（如果存在的话）。注意，Null值也会被更新。
     * @param object
     * @return
     */
    public int update(Id object);

    /**
     * Update。条件是id和update时间（如果存在的话）。注意，Null值不会被更新。
     * @param object
     * @return
     */
    public int updateNotNull(Id object);

    /**
     * Delete。条件是id和update时间（如果存在的话）。
     * @param object
     * @return
     */
    public int deleteById(Id object);


}
