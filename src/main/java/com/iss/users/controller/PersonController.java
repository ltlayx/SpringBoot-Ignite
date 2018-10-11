package com.iss.users.controller;

import com.iss.users.model.Person;
import com.iss.users.model.ReqPerson;
import com.iss.users.model.RespResult;
import com.iss.users.model.Role;
import com.iss.users.service.PersonService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request) throws UnknownHostException {
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getRemoteHost());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
//        String ip = null;
//
//        //X-Forwarded-For：Squid 服务代理
//        String ipAddresses = request.getHeader("X-Forwarded-For");
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //Proxy-Client-IP：apache 服务代理
//            ipAddresses = request.getHeader("Proxy-Client-IP");
//        }
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //WL-Proxy-Client-IP：weblogic 服务代理
//            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
//        }
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //HTTP_CLIENT_IP：有些代理服务器
//            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
//        }
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //X-Real-IP：nginx服务代理
//            ipAddresses = request.getHeader("X-Real-IP");
//        }
//
//        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
//        if (ipAddresses != null && ipAddresses.length() != 0) {
//            ip = ipAddresses.split(",")[0];
//        }
//
//        //还是不能获取到，最后再通过request.getRemoteAddr();获取
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ip = request.getRemoteAddr();
//        }
        return "";
    }


    /**
     * User register with whose username and password
     * @param reqPerson
     * @return Success message
     * @throws ServletException
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RespResult register(@RequestBody() ReqPerson reqPerson) throws ServletException {
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

        RespResult result = new RespResult();
        result.setStatuscode("201 CREATED");
        result.setMessage("register success");
        result.setData("");
        return result;
    }

    /**
     * Check user`s login info, then create a jwt token returned to front end
     * @param reqPerson
     * @return jwt token
     * @throws ServletException
     */
    @PostMapping
    public RespResult login(@RequestBody() ReqPerson reqPerson) throws ServletException {
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

        RespResult result = new RespResult();
        result.setStatuscode("200 OK");
        result.setMessage("login success");
        result.setData(jwtToken);
        return result;
    }
}
