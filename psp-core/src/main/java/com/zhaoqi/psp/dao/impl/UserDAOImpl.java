package com.zhaoqi.psp.dao.impl;

import com.zhaoqi.psp.dao.UserDAO;
import com.zhaoqi.psp.domain.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by qi on 17-11-13.
 */
@Repository
public class UserDAOImpl extends SqlSessionDaoSupport implements UserDAO {

    @Autowired
    private DataSource dataSource;

    @Override
    public void saveUser(User user) {
        String query = "insert into t_user (f_id, f_name, f_role) values (?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = dataSource.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getRole());
            int out = ps.executeUpdate();
            if(out !=0){
                System.out.println("Employee saved with id="+user.getId());
            }else System.out.println("Employee save failed with id="+user.getId());
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getUsers() {
        return null;
    }


    @Override
    public int removeUser(String id) {
        return 0;
    }
}
