package com.core.service.impl;

import com.core.constant.ExceptionConstant;
import com.core.dto.user.UserDto;
import com.core.entity.User;
import com.core.mapper.UserMapper;
import com.core.repository.UserRepository;
import com.core.service.UserService;
import com.core.utility.MethodLoggerUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public UserDto getUserDetails(String userName) {
        MethodLoggerUtility.start(this);

        User user = userRepository.findByUsernameAndIsActive(userName, true).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConstant.USER_NOT_FOUND)
        );

        MethodLoggerUtility.end(this);

        return userMapper.toDto(user);
    }
}
