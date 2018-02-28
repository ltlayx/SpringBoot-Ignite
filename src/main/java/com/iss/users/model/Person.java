package com.iss.users.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: users
 * @author: ISS12
 * @create: 2018-02-27 18:50
 **/

public class Person {

    private static final AtomicLong ID_GEN = new AtomicLong();

    /** Person ID (indexed) */
    @QuerySqlField(index = true)
    public long id;

    /** Person name(not-indexed) */
    @QuerySqlField
    public String name;

    /** Person phone(not-indexed) */
    @QuerySqlField
    public String phone;

    /**
     * Constructs Person record
     * @param id Person ID
     * @param name Person name
     * @param phone Person phone
     */
    public Person(long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Constructs Person record
     * @param name Person name.
     * @param phone Person phone.
     */
    public Person(String name, String phone) {
        // Generate unique ID for this person.
        this.id = ID_GEN.incrementAndGet();

        this.name = name;
        this.phone = phone;
    }

    /**
     * Get method
     * @return Person ID.
     */
    public long getId() {
        return id;
    }

    @Override
    public String toString(){
        return "Person [id=" + id +
                ", name=" + name +
                ", phone=" + phone + "]";
    }
}
