package com.example.notifyserver.keyword.service;

import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.keyword.domain.Keyword;
import com.example.notifyserver.keyword.dto.request.KeywordAddRequest;
import com.example.notifyserver.keyword.repository.KeywordRepository;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.dto.request.RegisterRequest;
import com.example.notifyserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.notifyserver.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    @Transactional
    public void addKeyword(final KeywordAddRequest request, final String googleId){
        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundUserException(USER_NOT_FOUND_EXCEPTION));
        Keyword newKeyword = Keyword.builder()
                .userKeyword(request.keyword())
                .user(user)
                .build();
        keywordRepository.save(newKeyword);
    }
}
