package com.zhaoqi.psp.domain;

import com.zhaoqi.psp.util.BeanUtil;

/**
 * Created by qi on 17-11-20.
 */
public class Id {
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return BeanUtil.hash(id);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return BeanUtil.equal(((Id) obj).getId(), id);
    }
}
