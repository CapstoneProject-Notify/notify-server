package com.example.notifyserver.user.service;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.common.service.EmailService;
import com.example.notifyserver.keyword.domain.Keyword;
import com.example.notifyserver.keyword.repository.KeywordRepository;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.dto.request.LoginRequest;
import com.example.notifyserver.user.dto.request.RegisterRequest;
import com.example.notifyserver.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.notifyserver.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final KeywordRepository keywordRepository;

    @Autowired
    private EmailService emailService;

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
    
    public void findAndSendEmail(Notice notice) {
        String noticeTitle = notice.getNoticeTitle();
        // 모든 키워드를 가져와서 사용자 ID에 해당하는 키워드 매핑
        Map<String, Set<Long>> keywordToUserIdsMap = new HashMap<>();
        List<Keyword> allKeywords = keywordRepository.findAll();
        for (Keyword keyword : allKeywords) {
            keywordToUserIdsMap.computeIfAbsent(keyword.getUserKeyword(), k -> new HashSet<>()).add(keyword.getUser().getUserId());
        }

        // notice title에 포함된 키워드를 찾아서 이메일 보내기
        for (String keyword : keywordToUserIdsMap.keySet()) {
            if (noticeTitle.contains(keyword)) {
                Set<Long> userIds = keywordToUserIdsMap.get(keyword);
                for (Long userId : userIds) {
                    // 해당 사용자 ID에 해당하는 이메일 찾기
                    Optional<User> optionalUser = userRepository.findById(userId);
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        // 이메일 보내기
                        sendKeywordEmail(user, keyword, notice);
                    }
                }
            }
        }
    }


    public void sendKeywordEmail(User user,String keyword, Notice notice) {
        String to = user.getEmail();
        String nickname =user.getNickName();
        Long postId = notice.getNoticeId();
        String postLink = notice.getNoticeUrl();

        emailService.sendNotificationEmail(to, nickname, keyword, postLink);
    }
}
