package com.zhaoqi.psp.domain;

/**
 * Created by zhaoqi on 12/11/2017.
 */
public class User {

    private int id;
    private String name;
    private String role;
    private String userId;
    private String firstName;
    private String lastName;

    public String getUserId() {
        return userId;
    }
    public boolean isLogined() {
        return id != 0;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
