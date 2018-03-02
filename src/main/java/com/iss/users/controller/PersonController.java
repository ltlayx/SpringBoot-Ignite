package com.iss.users.controller;

import com.iss.users.model.Person;
import com.iss.users.model.ReqPerson;
import com.iss.users.model.Role;
import com.iss.users.service.PersonService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.*;

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
     * User register with whose username and password
     * @param reqPerson
     * @return Success message
     * @throws ServletException
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody() ReqPerson reqPerson) throws ServletException {
        // Check if username and password is null
        if (reqPerson.getUsername() == "" || reqPerson.getUsername() == null
                || reqPerson.getPassword() == "" || reqPerson.getPassword() == null)
            throw new ServletException("Username or Password invalid!");

        // Check if the username is used
        if(personService.findPersonByUsername(reqPerson.getUsername()) != null)
            throw new ServletException("Username is used!");

        // Give a default role : MEMBER
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.MEMBER);

        // Create a person in ignite
        personService.save(new Person(reqPerson.getUsername(), reqPerson.getPassword(), roles));
        return "Register Success!";
    }

    /**
     * Check user`s login info, then create a jwt token returned to front end
     * @param reqPerson
     * @return jwt token
     * @throws ServletException
     */
    @PostMapping
    public String login(@RequestBody() ReqPerson reqPerson) throws ServletException {
        // Check if username and password is null
        if (reqPerson.getUsername() == "" || reqPerson.getUsername() == null
                || reqPerson.getPassword() == "" || reqPerson.getPassword() == null)
            throw new ServletException("Please fill in username and password");

        // Check if the username is used
        if(personService.findPersonByUsername(reqPerson.getUsername()) == null
                || !reqPerson.getPassword().equals(personService.findPersonByUsername(reqPerson.getUsername()).getPassword())){
            throw new ServletException("Please fill in username and password");
        }

        // Create Twt token
        String jwtToken = Jwts.builder().setSubject(reqPerson.getUsername()).claim("roles", "member").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return jwtToken;
    }
}
