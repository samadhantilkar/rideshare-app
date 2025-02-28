package com.rideshare.rideshareapi.security;

import com.rideshare.rideshareapi.account.Account;
import com.rideshare.rideshareapi.account.AccountService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AccountService accountService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            final String requestTokenHeader=request.getHeader("Authorization");

            if(null==requestTokenHeader || !requestTokenHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }

            String token=requestTokenHeader.split("Bearer ")[1];

            Long accountId= jwtService.getAccountIdFromToken(token);

            if(null != accountId && SecurityContextHolder.getContext().getAuthentication() == null){

                Account account=accountService.getAccountById(accountId);

                //check account should be  allowed
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(account,null,account.getAuthorities());

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request,response);
        }catch (JwtException ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}
