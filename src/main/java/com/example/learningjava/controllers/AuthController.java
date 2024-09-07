package com.example.learningjava.controllers;

import com.example.learningjava.dtos.JsonResponseMessage;
import com.example.learningjava.dtos.UserLoginDto;
import com.example.learningjava.dtos.UserRegistrationDto;
import com.example.learningjava.models.User;
import com.example.learningjava.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userDto) {
        User user = authService.signup(userDto);
        Map<String, Object> body = new HashMap<>();
        body.put("details", String.format("username %s created with user id : %d", user.getUsername(), user.getId()) );
        return ResponseEntity.ok(body);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto userDto) {
        try {
            String token = authService.login(userDto);
            Map<String, Object> body = new HashMap<>();
            body.put("token", token);
            return ResponseEntity.ok(body);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResponseMessage.builder()
                    .Msg("Invalid Credentials").code("0").build());
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResponseMessage.builder()
                    .Msg("Account is locked").code("1").build());
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonResponseMessage.builder()
                    .Msg("Account is disabled Please Contact The Administrator for enable this account").code("3").build());
        }
    }
}
