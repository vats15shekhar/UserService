package dev.vatsala.UserService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SpringSecurity {

    @Order(1)
    @Bean
    public SecurityFilterChain filteringCriteria(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable();
//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/*").permitAll());
//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/order/*").authenticated());
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
    /*
    The above is the filtering method we use, in order to permit or restrict the access to an API from the
    client side. All of this is performed by Spring Security. Right now we are allowing access to all the APIs.
    Now if we try to signup, there won't be any errors.
     */


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

/*
We need this configuration class in order for us to be able to use the Bcrypt password encoder object.
So we use the annotation @configuration. And the method we have created will return a BcryptPasswordEncoder Object
It is a Bean because it needs to be injected Where we are creating the object in AuthService Class.

For us to be able to use the object, this configuration class is required
 */
