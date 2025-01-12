package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    DataSource dataSource; //creates a bean of DataSource
    //Autowired injects Datasource dpenddencie in datasource
    @Bean
    //we are returning a filter chain hence func type is that and argument is of httpsecurity type (remember these two things)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.requestMatchers("/h2-console/**").permitAll()
                        .anyRequest()).authenticated()); //if this h2 requst appears allow everyon no login page neede
        http.sessionManagement(session->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         //http.formLogin(Customizer.withDefaults());
        //toenable the framse if u dont see h2
        http.headers(headers->headers.frameOptions(frameOptions->frameOptions.sameOrigin()));
        http.csrf(csrf-> csrf.disable());
        http.httpBasic(Customizer.withDefaults());

        return (SecurityFilterChain) http.build();
    }
    //In memory authentication
    @Bean
    //its predefine function --> UserDetailsService
    public UserDetailsService userDetailsService() {
        //theres a class UserDetails thats used to deign user details
        UserDetails user1= User.withUsername("user1")
                .password(passwordEncoder().encode("password1")) //noop-->to save password as plain text
                .roles("USER")
                .build(); //created a user here itself
        UserDetails admin= User.withUsername("admin")
                .password(passwordEncoder().encode("password2")) //noop-->to encode ur password
                .roles("ADMIN")
                .build();

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(admin);
        return userDetailsManager;
        //return new InMemoryUserDetailsManager(user1,admin); //it manages user details in memory
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
