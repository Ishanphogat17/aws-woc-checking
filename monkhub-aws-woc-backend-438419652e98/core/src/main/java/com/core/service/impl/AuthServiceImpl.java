package com.core.service.impl;

import com.core.dto.auth.LoginRequestDto;
import com.core.dto.auth.SignupRequestDto;
import com.core.dto.user.UserDto;
import com.core.repository.UserRepository;
import com.core.security.JwtTokenHelper;
import com.core.service.AuthService;
import com.core.service.UserService;
import com.core.utility.MethodLoggerUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestDto loginRequestDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );
        UserDetails user = User.withUsername(loginRequestDto.getUsername())
                .password(loginRequestDto.getPassword())
                .build();
        UserDto userDto = userService.getUserDetails(loginRequestDto.getUsername());
        return jwtTokenHelper.generateToken(user.getUsername(), userDto.getUserId());
    }

    @Override
    public Optional<String> createPasswordResetTokenForUsername(String username) {
        MethodLoggerUtility.start(this);
        UserDto userDto = userService.getUserDetails(username);
        if(userDto == null) {
            MethodLoggerUtility.end(this);
            return Optional.empty();
        }

        String token = jwtTokenHelper.createResetToken(username);
        // In production: send token to user's email. For demo: return it.
        MethodLoggerUtility.end(this);
        return Optional.of(token);
    }

    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        if (!jwtTokenHelper.isResetTokenValid(token)) return false;

        String username = jwtTokenHelper.getUsernameFromToken(token);
        com.core.entity.User user = userRepository.findByUsernameAndIsActive(username,true).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        if (user == null) return false;

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    @Override
    public String signup(SignupRequestDto signupRequestDto){
        if (userRepository.existsByUsernameAndIsActive(signupRequestDto.getUsername(),true))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");

        if (userRepository.existsByEmailAndIsActive(signupRequestDto.getEmail(),true))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");

        com.core.entity.User userEntity = com.core.entity.User.builder()
                .username(signupRequestDto.getUsername())
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();

        com.core.entity.User savedUser = userRepository.save(userEntity);


        return jwtTokenHelper.generateToken(signupRequestDto.getUsername(), savedUser.getUserId());
    }
}
