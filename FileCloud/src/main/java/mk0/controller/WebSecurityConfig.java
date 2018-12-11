/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.controller;

import mk0.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {    
    
    @Autowired
    private UserServiceImpl userDetailsService;
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()                
                .antMatchers("/manageUsers", "/h2-console/**", "/helloAdmin").hasRole("ADMIN")
                .antMatchers("/uploadFile", "/manageAccount").hasAnyRole("ADMIN", "USER")
                .antMatchers("/newUser/**").hasRole("ADMIN").anyRequest().authenticated()
                .antMatchers("/helloUser").hasRole("USER")                
                .and()
            .formLogin().loginPage("/home").permitAll()
                .and()
            .logout().permitAll();
            
        //Uncomment if h2-console is desired
        //http.csrf().disable();
        //http.headers().frameOptions().disable();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    
}
