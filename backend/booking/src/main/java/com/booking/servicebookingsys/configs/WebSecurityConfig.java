package com.booking.servicebookingsys.configs;

import com.booking.servicebookingsys.services.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity      // 啟用spring security的web安全性功能，將 HttpSecurity 類型的 Bean 自動添加到 Spring IoC 容器中。
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
        csrf(AbstractHttpConfigurer::disable)：停用跨站請求保護。這樣才可直接由外部打 API，無論是實務上前端造訪，或是測試時用 Postman 造訪才能通過。
        authorizeHttpRequests() ：設定授權規則的開頭。
        authenticated()：需要驗證。
        permitAll()：不需要驗證，對所有角色開放。
        SessionCreationPolicy.STATELESS ： 不建立Session。cookies不被使用，每個請求需要被驗證。這邊用jwt取代session。
         */

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/hello","/authenticate","/company/sign-up","/client/sign-up", "/ads","/search/{service}").permitAll()
                        .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    // configuration 規定了哪些 API 需要通過驗證，這邊設定驗證的方法。
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // spring boot use this bean in the process of validating the user's password.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
