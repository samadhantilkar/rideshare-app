package com.rideshare.rideshareapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class RideShareApiSecurityConfig{

    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionCongfig ->sessionCongfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/passenger/**").hasAnyRole("PASSENGER","ADMIN")
                            .requestMatchers("/driver/**").hasAnyRole("DRIVER","ADMIN")
                            .requestMatchers("/location/**").hasAnyRole("DRIVER","ADMIN","PASSENGER")
                                .anyRequest().permitAll()
                )
                .exceptionHandling(exhandling -> exhandling.accessDeniedHandler(accessDeniedHandler()));

        return httpSecurity.build();

    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request,response,accessDeniedException) ->{
            handlerExceptionResolver.resolveException(request,response,null,accessDeniedException);
        };
    }

//    public static class APISecurityConfig extends WebSecurityConfigurerAdapter{
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception{
//            http.authorizeRequests()
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .antMatchers("/passenger/**").hasAnyRole("PASSENGER","ADMIN")
//                    .antMatchers("/driver/**").hasAnyRole("DRIVER","ADMIN")
//                    .antMatchers("/location/**").hasAnyRole("DRIVER","ADMIN","PASSENGER")
////                    .and().apply(new JwtConfigurer(this.tokenProvider))
//                    .and()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        }
//    }
//
//    @Configuration
//    @Order(2)
//    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter{
//
//        @Configuration
//        public static class WebSecurityAdapter extends WebSecurityConfigurerAdapter{
//
//            @Override
//            protected void configure(HttpSecurity http) throws Exception {
//                http.authorizeRequests().antMatchers("/").permitAll()
//                        .antMatchers("/register/**").permitAll()
//                        .antMatchers("/db-init/**").permitAll()
//                        .antMatchers("/login/**").permitAll()
//                        .antMatchers("/logout/**").permitAll()
//                        .and().formLogin()
//                        .and().csrf();
//            }
//        }
//    }



}
