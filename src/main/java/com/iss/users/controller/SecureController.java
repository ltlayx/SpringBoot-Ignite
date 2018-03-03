package com.iss.users.controller;

import com.iss.users.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Test the jwt, if the token is valid then return "Login Successful"
 * If is not valid, the request will be intercepted by JwtFilter
 * @program: users
 * @author: 李泰郎
 * @create: 2018-03-01 11:05
 **/
@RestController
@RequestMapping("/secure")
public class SecureController {

    @RequestMapping("/users/user")
    public String loginSuccess() {
        return "Login Successful!";
    }

//    @PostMapping("/user/roles")
//    public String checkRoles(){
//        return personService.findPersonByUsername()
//    }

}