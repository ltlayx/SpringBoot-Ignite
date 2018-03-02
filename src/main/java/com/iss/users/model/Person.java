package com.iss.users.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 18:50
 **/

public class Person {

    private static final AtomicLong ID_GEN = new AtomicLong();

    /** Person ID (indexed) */
    @QuerySqlField(index = true)
    private long id;

    /** Person name(indexed) */
    @QuerySqlField(index = true)
    private String username;

    /** Person phone(not-indexed) */
    @QuerySqlField
    private String password;

    /** Person roles(not-indexed) */
    @QuerySqlField
    private List<Role> roles;

    public Person() {
    }

    public Person(long id, String name, String password, List<Role> roles) {
        this.id = id;
        this.username = name;
        this.password = password;
        this.roles = roles;
    }

    public Person(String name, String password, List<Role> roles) {
        this.id = ID_GEN.incrementAndGet();
        this.username = name;
        this.password = password;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString(){
        return "Person [id=" + id +
                ", username=" + username +
                ", password=" + password +
                ", roles=" + roles.toString() + "]";
    }
}
