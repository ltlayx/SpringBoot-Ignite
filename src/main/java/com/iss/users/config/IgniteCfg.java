package com.iss.users.config;

import com.iss.users.model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
