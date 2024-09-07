package com.example.learningjava.service;

import com.example.learningjava.config.jwt.JwtUtils;
import com.example.learningjava.dtos.JsonResponseMessage;
import com.example.learningjava.dtos.UserLoginDto;
import com.example.learningjava.dtos.UserRegistrationDto;
import com.example.learningjava.models.User;
import com.example.learningjava.repositories.RoleRepository;
import com.example.learningjava.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.learningjava.exceptions.UserAlreadyExistedException;
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public User signup(UserRegistrationDto userRegistrationDto) {
        // Check if username or email already exists
        if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistedException("Username is already taken");
        }
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        // Save the User into database
        return userRepository.save(user);
    }
    public String login(UserLoginDto loginRequest) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            return jwt;
    }
}
