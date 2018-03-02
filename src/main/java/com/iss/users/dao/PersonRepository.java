package com.iss.users.dao;

import com.iss.users.model.Person;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;


/**
 * Apache Ignite Spring Data repository backed by Ignite Person's cache.
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 18:50
 **/

@RepositoryConfig(cacheName = "PersonCache")
public interface PersonRepository extends IgniteRepository<Person, Long>{

    /**
     * Find a person with given name in Ignite DB.
     * @param name Person name.
     * @return The person whose name is the given name.
     */
    Person findByUsername(String name);

}
