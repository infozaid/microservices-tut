package com.PhotoApi.ws.users.security;

import com.PhotoApi.ws.users.dto.UserDto;
import com.PhotoApi.ws.users.service.UserService;
import com.PhotoApi.ws.users.ui.payload.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;
    private AuthenticationManager authenticationManager;


    public AuthenticationFilter(UserService userService, Environment environment, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment environment, AuthenticationManager authenticationManager1) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
        this.authenticationManager = authenticationManager1;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{

            LoginRequestModel creds=new ObjectMapper().readValue(request.getInputStream(),LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(),creds.getPassword(),new ArrayDeque<>())
            );

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
                 String userName=((User)authResult.getPrincipal()).getUsername();
                UserDto userDetails= userService.getUserDetailsByEmail(userName);

                 String token = Jwts.builder().setSubject(userDetails.getUserId())
                         .setSubject(userDetails.getUserId())
                         .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(environment.getProperty("token.expiration"))))
                         .signWith(SignatureAlgorithm.HS256,environment.getProperty("token.secret"))
                         .compact();
                 response.addHeader("token",token);
                 response.addHeader("userId",userDetails.getUserId());

    }


    }
