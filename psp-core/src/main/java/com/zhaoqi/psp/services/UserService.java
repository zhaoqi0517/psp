package com.zhaoqi.psp.services;

import com.zhaoqi.psp.domain.LoginData;
import com.zhaoqi.psp.domain.User;

/**
 * Created by zhaoqi on 12/11/2017.
 */

public interface UserService {

    public void save(User user);

    public User login(LoginData data) throws Exception;
}
