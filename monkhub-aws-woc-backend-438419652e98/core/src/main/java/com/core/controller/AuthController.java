package com.core.controller;

import com.core.constant.ApiConstant;
import com.core.dto.auth.ForgotPasswordRequest;
import com.core.dto.auth.LoginRequestDto;
import com.core.dto.auth.ResetPasswordRequest;
import com.core.dto.auth.SignupRequestDto;
import com.core.dto.response.ResponseDto;
import com.core.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = ApiConstant.AUTH)
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDto> updateStatus(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Login successful").data(token).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(ResponseDto.builder().status(false).message("Missing or invalid Authorization header").build());
        }

        return ResponseEntity.badRequest().body(ResponseDto.builder().status(true).message("Logged out successfully").build());
    }

//    @PostMapping("/forgot-password")
//    public ResponseEntity<ResponseDto> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
//        String token = authService.createPasswordResetTokenForUsername(request.getUsername()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
//        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Password reset token generated").data(token).build());
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<ResponseDto> resetPassword(@RequestBody @Valid ResetPasswordRequest req) {
//        boolean ok = authService.resetPassword(req.getToken(), req.getNewPassword());
//        if (!ok) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token");
//        }
//        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Password reset successful").build());
//    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        String token = authService.signup(signupRequestDto);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Signup successful").data(token).build());
    }
}
