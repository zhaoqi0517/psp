package com.zhaoqi.psp.dao;

import com.zhaoqi.psp.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaoqi on 12/11/2017.
 */
public interface UserDAO {

    public void saveUser(User user);

    public List<User> getUsers();

    public int removeUser(String id);
}
