package com.example.demo.config;
import ch.qos.logback.classic.encoder.JsonEncoder;
//import com.example.demo.controller.RegistrationController;
//import com.example.demo.services.UserService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registration", "/css/**").permitAll()

                        .requestMatchers("/new", "/save", "/edit/**", "/delete/**").hasRole("ADMIN")
                        .requestMatchers("/bookings/create", "/bookings/history").authenticated()
                        .requestMatchers("/bookings/*/cancel").authenticated()
                        .requestMatchers("/bookings/*/restore").hasRole("ADMIN")

                        .requestMatchers("/stats").hasRole("ADMIN")

                        .requestMatchers("/users", "/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//    @Bean%
//   public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//    return new UserService();
//    }

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        return userDetailsManager;
//    }


//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user")
//                        .password(passwordEncoder.encode("user"))
//                        .roles("USER")
//                        .build();
//        UserDetails admin =
//                User.builder()
//                        .username("admin")
//                        .password(passwordEncoder.encode("admin"))
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//        //return new UserService();
//    }

//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .usersByUsernameQuery("select username, password, active from usr where username=?")
//                .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");
//    }


