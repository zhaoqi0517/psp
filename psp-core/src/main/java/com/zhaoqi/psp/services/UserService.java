package com.zhaoqi.psp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;

/**
 * Created by zhaoqi on 12/11/2017.
 */
@Component
public class UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    private String name;

    public UserService() {
    }

    public String getName() {
        if (redisTemplate.hasKey("name")) {
            return "Cached :" + redisTemplate.boundListOps("name").leftPop();
        }
        return "Hello," +  name;
    }

    public void setName(String name) {
        this.name = name;
        listOps.leftPush("name", this.name);
    }

}
