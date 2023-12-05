package com.JWTpostegree.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.JWTpostegree.demo.model.Users;

import com.JWTpostegree.demo.repository.UserRepository;
import com.JWTpostegree.demo.utils.JwtTokenUtil;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;



    @Controller
    @RequestMapping
    @OpenAPIDefinition
public class ControllerApi {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
   


//--------------------------------------------------------------------------------------------





   











}