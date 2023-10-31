package com.techgeek.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // UserDetails is prepared and stored to InMemory.
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("Vijay")
                .password(passwordEncoder().encode("Pwd1"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("Kumar")
                .password(passwordEncoder().encode("Pwd2"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);

        // If UserDetails fetches from database then use DaoAuthenticationProvider
        // Create UserInfoUSerDetailsService interface and connect to Repository and then DB and
        // fetch the User name, password and roles

        //return new UserInfoUSerDetailsService();
    }

    /*
     Below changes for Spring boot 3.0.1
    **/

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        return http.csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/products/hello").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().and().build();
//    }

    /*
     Changes made below for Spring boot 3.1.1
    // Few classes has deprecated
    **/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/products/hello", "/products/new")
                                .permitAll()
                                .requestMatchers("/products/**")
                                .authenticated()
                )
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // If UserDetails fetches from database then use DaoAuthenticationProvider

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//
//        return authenticationProvider;
//    }

}
