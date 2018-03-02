package com.iss.users.model;

/**
 * @program: users
 * @author: 李泰郎
 * @create: 2018-03-02 10:44
 * Roles：
 * ADMIN & MEMBER
 **/
public enum Role {
    ADMIN, MEMBER;

    public String authority(){
        return "ROLE_" + this.name();
    }

    @Override
    public String toString(){
        return this.name();
    }
}
