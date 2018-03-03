package com.iss.users.config;

import com.iss.users.model.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is Jwt configuration which set the url "/secure/*" for filtering
 * @program: users
 * @author: 李泰郎
 * @create: 2018-03-03 21:18
 **/
@Configuration
public class JwtCfg {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/secure/*");

        return registrationBean;
    }

}
