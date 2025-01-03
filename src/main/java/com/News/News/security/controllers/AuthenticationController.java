package com.News.News.security.controllers;

import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.security.dtos.LoginRequest;
import com.News.News.security.model.CustomUserDetails;
import com.News.News.security.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            //String username = userDetails.getUsername();
            //String role = userDetails.getAuthorities().stream().findFirst().get().toString();
            List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtService.generateToken(userDetails.getUsername(), userDetails.getUserId() ,authorities));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AuthenticationException e) {
            e.printStackTrace();// Log the exact exception
            if(e.getMessage().contains("User not found"))
            {
                throw new AppException("User not found", ErrorCode.NOT_FOUND);
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication failed");
        }
    }


}
