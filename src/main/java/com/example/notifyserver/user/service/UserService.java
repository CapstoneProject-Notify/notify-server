package com.example.notifyserver.user.service;

import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.keyword.domain.Keyword;
import com.example.notifyserver.keyword.repository.KeywordRepository;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.dto.request.LoginRequest;
import com.example.notifyserver.user.dto.request.RegisterRequest;
import com.example.notifyserver.user.dto.response.UserProfileResponse;
import com.example.notifyserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.notifyserver.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final KeywordRepository keywordRepository;

    public void userLogin(final LoginRequest request){
        String googleId = request.googleId();
        Optional<User> findUser = userRepository.findByGoogleId(googleId);
        if(findUser.isEmpty()){
            User newUser = User.builder()
                    .googleId(googleId)
                    .build();

            userRepository.save(newUser);
            throw new NotFoundUserException(USER_NOT_FOUND_EXCEPTION);
        } else if(findUser.get().getEmail() == null){
            throw new NotFoundUserException(USER_NOT_FOUND_EXCEPTION);
        }
    }

    @Transactional
    public void userRegister(final RegisterRequest request, final String googleId){
        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundUserException(USER_NOT_FOUND_EXCEPTION));
        user.update(request.nickname(), request.email(), request.userMajor());
    }

    @Transactional
    public void userWithdraw(final String googleId){
        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundUserException(USER_NOT_FOUND_EXCEPTION));
        Long userId = user.getUserId();
        scrapRepository.deleteAllByUser(user);
        keywordRepository.deleteAllByUser(user);
        userRepository.deleteByUserId(userId);
    }

    @Transactional
    public UserProfileResponse getUserProfile(final String googleId){
        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundUserException(USER_NOT_FOUND_EXCEPTION));
        Long userId = user.getUserId();
        String nickName = user.getNickName();
        String email = user.getEmail();
        String major = user.getUserMajor().getValue();
        List <String> keywords = keywordRepository.findAllByUserUserId(userId)
                .stream()
                .map(Keyword::getUserKeyword)
                .collect(Collectors.toList());;

        UserProfileResponse userProfileResponse = new UserProfileResponse(nickName, email, major, keywords);
        return userProfileResponse;
    }
}
