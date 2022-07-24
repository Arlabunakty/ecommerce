package ua.training.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ua.training.ecommerce.security.AuthenticationFailureHandler;
import ua.training.ecommerce.security.AuthenticationSuccessHandler;
import ua.training.ecommerce.security.SecurityProperties;
import ua.training.ecommerce.security.TokenAuthenticationFilter;
import ua.training.ecommerce.security.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter(TokenService tokenService, UserDetailsService userDetailsService, SecurityProperties properties) {
        return new TokenAuthenticationFilter(tokenService, userDetailsService, properties.getHeader());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenAuthenticationFilter jwtAuthenticationTokenFilter, AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationTokenFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/product/image/**").permitAll()
                .antMatchers("/product/**").permitAll()
                .antMatchers("/group/**").permitAll()
                .antMatchers("/cart/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .anyRequest().permitAll()
                .and().formLogin().successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .csrf().disable();
        return http.build();
    }

}
