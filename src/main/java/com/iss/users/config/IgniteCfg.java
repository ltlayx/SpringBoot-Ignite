package com.iss.users.config;

import com.iss.users.model.Person;
import com.iss.users.model.Role;
import com.iss.users.service.PersonService;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 19:08
 **/

@Configuration
public class IgniteCfg {

    /**
     * 初始化ignite节点信息
     * @return Ignite
     */
    @Bean
    public Ignite igniteInstance(){
        // 配置IgniteConfiguration
        IgniteConfiguration cfg = new IgniteConfiguration();

        // 设置节点名称
        cfg.setIgniteInstanceName("springDataNode");

        // 启用Peer类加载器
        cfg.setPeerClassLoadingEnabled(true);

        // 创建一个新的Cache以供Ignite节点使用
        CacheConfiguration ccfg = new CacheConfiguration("PersonCache");

        // 设置SQL的Schema
        ccfg.setIndexedTypes(Long.class, Person.class);

        cfg.setCacheConfiguration(ccfg);

        return Ignition.start(cfg);
    }


    @Autowired
    PersonService personService;
    /**
     * Add few people in ignite for testing easily
     */
    @Bean
    public int addPerson(){
        // Give a default role : MEMBER
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.MEMBER);

        // add data
        personService.save(new Person("test1", "test1", roles));
        personService.save(new Person("test2", "test2", roles));
        personService.save(new Person("test3", "test3", roles));

        return 0;
    }
}
