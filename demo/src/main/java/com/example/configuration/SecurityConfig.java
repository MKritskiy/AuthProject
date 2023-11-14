package com.example.configuration;

import com.example.security.RightChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    RightChecker rightChecker;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.DELETE,"/api/book/{id}", "/api/telephone/{id}", "/api/washingMachine/{id}").access(rightChecker)
                        .requestMatchers(HttpMethod.DELETE, "/api/book", "/api/telephone", "/api/washingMachine").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/book", "/api/telephone", "/api/washingMachine").access(rightChecker)
                        .requestMatchers(HttpMethod.PUT, "/api/book/{id}", "/api/telephone/{id}", "/api/washingMachine/{id}").access(rightChecker)
                        .requestMatchers("/**").permitAll()
                )
                .sessionManagement((sessionManagment) -> sessionManagment
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf((x) -> x.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

}
