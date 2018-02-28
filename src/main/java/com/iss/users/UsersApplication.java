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
 * 1. 使用最简单的配置启动一个Ignite节点，包含一个名为PersonCache的Cache，ignite随Web项目启动与停止
 * 2. 提供api接口实现RESTful的设计，能够通过api添加与查询Cache中的相关内容
 **/

/**
 * 项目启动入口，配置@EnableIgniteRepositories注解以支持ignite的RepositoryConfig
 */
@SpringBootApplication
@EnableIgniteRepositories
public class UsersApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}
}
