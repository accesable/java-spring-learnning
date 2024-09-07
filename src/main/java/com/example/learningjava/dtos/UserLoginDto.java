package com.example.learningjava.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Getters and setters
}

