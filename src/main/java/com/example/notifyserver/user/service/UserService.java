package com.example.notifyserver.user.service;

import com.example.notifyserver.common.exception.model.NotFoundException;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.dto.request.LoginRequest;
import com.example.notifyserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.notifyserver.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void userLogin(final LoginRequest request){
        String googleId = request.googleId();
        Optional<User> findUser = userRepository.findByGoogleId(googleId);
        if(findUser.isEmpty()){
            User newUser = User.builder()
                    .googleId(googleId)
                    .build();

            userRepository.save(newUser);
            throw new NotFoundUserException(USER_NOT_FOUND_EXCEPTION);
        }
    }

    @Transactional
    public void userRegister(final String googleId){
        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));

    }
}
