package com.example.notifyserver.notice.service;

import com.example.notifyserver.common.constants.NoticeConstants;
import com.example.notifyserver.common.domain.NoticeCategory;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.notice.domain.Notice;
import com.example.notifyserver.notice.dto.NoticeResponse;
import com.example.notifyserver.notice.repository.NoticeRepository;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.notifyserver.common.exception.enums.ErrorCode.*;

@Service
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository, ScrapRepository scrapRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.scrapRepository = scrapRepository;
        this.userRepository = userRepository;
    }

    /**
     * 로그인 하지 않은 사용자가 공지사항을 조회한다.
     * @param type 공지사항 종류
     * @param pageNum 조회할 페이지 번호
     * @param category 조회할 카테고리
     * @return 페이지 정보 및 공지사항들
     * @throws Exception 서버 내부 오류 및 유효하지 않은 값 입력 오류
     */
    @Override
    public Page<NoticeResponse> getNoticesWithoutLogin(NoticeType type, int pageNum, NoticeCategory category) throws Exception {
        Pageable pageable = PageRequest.of(pageNum-1, (int) NoticeConstants.PAGE_SIZE);
        try {
            Page<Notice> findNotices;
            if(category == NoticeCategory.ALL){
                findNotices = noticeRepository.findAllByNoticeType(type, pageable);
            }else {
                findNotices = noticeRepository.findAllByNoticeTypeAndCategory(type, pageable,category);
            }
            Page<NoticeResponse> noticeResponses = findNotices.map(findNotice -> new NoticeResponse(
                    findNotice.getNoticeId(),
                    findNotice.getNoticeTitle(),
                    findNotice.getNoticeDate(),
                    false,
                    findNotice.getNoticeUrl(),
                    findNotice.getNoticeCategory().toString().toLowerCase()
            ));
            return noticeResponses;
        }catch (NotFoundUserException e) {
            throw new ValidationException(VALIDATION_REQUEST_MISSING_EXCEPTION.getMessage());
        } catch (Exception e) {
            throw new Exception(INTERNAL_SERVER_EXCEPTION.getMessage());
        }

    }

    /**
     * 로그인한 사용자가 공지사항을 조회한다.
     * @param type 공지사항 종류
     * @param pageNum 조회할 페이지 번호
     * @param category 조회할 카테고리
     * @return 페이지 정보 및 공지사항들
     * @throws Exception 서버 내부 오류 및 유효하지 않은 값 입력 오류
     */
    @Override
    public Page<NoticeResponse> getNoticesWithLogin(String googleId, NoticeType type, int pageNum, NoticeCategory category) throws Exception {
        User user = userRepository.findByGoogleId(googleId).orElseThrow(() -> new NotFoundUserException(USER_NOT_FOUND_EXCEPTION));
        Pageable pageable = PageRequest.of(pageNum-1, (int) NoticeConstants.PAGE_SIZE);
        try {
            Page<Notice> findNotices;
            if(category == NoticeCategory.ALL){
                findNotices = noticeRepository.findAllByNoticeType(type, pageable);
            }else {
                findNotices = noticeRepository.findAllByNoticeTypeAndCategory(type, pageable,category);
            }
            Page<NoticeResponse> noticeResponses = findNotices.map(findNotice -> {
                boolean isScrapped = scrapRepository.existsByUserIdAndNoticeIdAndType(user.getUserId(), findNotice.getNoticeId(), type);
                return new NoticeResponse(
                        findNotice.getNoticeId(),
                        findNotice.getNoticeTitle(),
                        findNotice.getNoticeDate(),
                        isScrapped,
                        findNotice.getNoticeUrl(),
                        findNotice.getNoticeCategory().toString().toLowerCase()
                );
            });
            return noticeResponses;
        }catch (NotFoundUserException e) {
            throw new ValidationException(VALIDATION_REQUEST_MISSING_EXCEPTION.getMessage());
        } catch (Exception e) {
            throw new Exception(INTERNAL_SERVER_EXCEPTION.getMessage());
        }
    }
}
