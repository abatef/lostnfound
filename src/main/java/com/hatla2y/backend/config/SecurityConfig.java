package com.hatla2y.backend.config;

import com.hatla2y.backend.filters.FirebaseAuthenticationFilter;
import com.hatla2y.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public FirebaseAuthenticationFilter firebaseAuthenticationFilter(UserRepository userRepository) {
        FirebaseAuthenticationFilter filter = new FirebaseAuthenticationFilter(userRepository);
        filter.addExceptionMatcher(new AntPathRequestMatcher("/api/v1/user/pub/me", "GET"));
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(req -> {
           req.requestMatchers("/api/v1/user/info").authenticated();
           req.anyRequest().permitAll();
        });
        http.addFilterBefore(firebaseAuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
