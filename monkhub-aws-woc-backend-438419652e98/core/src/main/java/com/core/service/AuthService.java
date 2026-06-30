package com.core.service;

import com.core.dto.auth.LoginRequestDto;
import com.core.dto.auth.SignupRequestDto;

import java.util.Optional;

public interface AuthService {
    String login(LoginRequestDto loginRequestDto);

    Optional<String> createPasswordResetTokenForUsername(String username);

    boolean resetPassword(String token, String newPassword);

    String signup(SignupRequestDto signupRequestDto);
}
