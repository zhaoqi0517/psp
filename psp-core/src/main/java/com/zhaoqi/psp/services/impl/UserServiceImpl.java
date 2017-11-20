package com.zhaoqi.psp.services.impl;

import com.zhaoqi.psp.dao.UserDAO;
import com.zhaoqi.psp.domain.LoginData;
import com.zhaoqi.psp.domain.User;
import com.zhaoqi.psp.exception.ServiceException;
import com.zhaoqi.psp.services.UserService;
import com.zhaoqi.psp.util.AppUtil;
import com.zhaoqi.psp.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by qi on 17-11-14.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    @Autowired
    private UserDAO userDAO;

    private String name;

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

    public User login(LoginData data) throws Exception {

        LoginData condition = BeanUtil.copy(data);

        String password = condition.getPassword();
        condition.setPassword(AppUtil.getMD5(password));

        PaginationCondition pc = new PaginationCondition();
        pc.setLimit(10);
        userDAO.find(pc);

        User user = userDAO.find(condition);
        if (user == null) {
            throw new ServiceException("exception.service.loginfailed", null);
        }

        return user;
    }

    @Override
    public void save(User user) {
        userDAO.saveUser(user);
    }
}
