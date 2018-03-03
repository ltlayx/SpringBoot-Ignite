package com.iss.users;

import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 19:28
 *
 * 该项目是用于将Ignite部署到SpringBoot上的一个测试性的项目
 * 目前的功能包含：
 * 1. 启动并使用一个ignite节点
 * 2. 提供api接口实现RESTful的设计，能够通过api添加与查询Cache中的相关内容
 **/

/**
 * It's a test project for deploying Ignite on SpringBoot
 * Function:
 * 	1.Start an ignite node
 * 	2.provide RESTful api to create or retrieve information in Ignite Cache
 * Here are the apis:
 * 		/person?name=XXX&phone=XXX	get,	store the person in Ignite and return a json of the person
 * 		/persons?name=xxx 			get,	return a json of the person
 */

/**
 * 项目启动入口，配置@EnableIgniteRepositories注解以支持ignite的@RepositoryConfig
 */
@SpringBootApplication
@EnableIgniteRepositories
public class UsersApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}
}