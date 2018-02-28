package com.iss.users.controller;

import com.iss.users.model.Person;
import com.iss.users.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 19:28
 **/
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Save a person with name and phone sent by front end.
     * @param name Person name.
     * @param phone Person phone.
     * @return The person saved in ignite DB
     */
    @RequestMapping("/person")
    public Person savePerson(@RequestParam(value = "name") String name, @RequestParam(value = "phone") String phone){
        return personService.save(new Person(name, phone));
    }

    /**
     * Find a person with given name sent by front end.
     * @param name Person name.
     * @return The person found in ignite DB
     */
    @RequestMapping("/persons")
    public Person savePerson(@RequestParam(value = "name") String name){
        return personService.findPersonByName(name);
    }

}
