package com.iss.users.service.impl;

import com.iss.users.dao.PersonRepository;
import com.iss.users.model.Person;
import com.iss.users.service.PersonService;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements interface PersonService
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 19:20
 **/

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;

    /**
     * Save Person to Ignite DB
     * @param person Person object.
     * @return The Person object saved in Ignite DB.
     */
    public Person save(Person person) {
        return personRepository.save(person.getId(), person);
    }

    /**
     * Find a Person from Ignite DB with given name.
     * @param name Person name.
     * @return The person found in Ignite DB
     */
    public Person findPersonByName(String name){
        return personRepository.findByName(name);
    }

}
