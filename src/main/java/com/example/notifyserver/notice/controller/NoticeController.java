package com.example.notifyserver.notice.controller;

import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.common.dto.ApiResponse;
import com.example.notifyserver.common.dto.ErrorResponse;
import com.example.notifyserver.common.dto.SuccessResponse;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import com.example.notifyserver.common.exception.enums.SuccessCode;
import com.example.notifyserver.notice.dto.NoticeResponse;
import com.example.notifyserver.notice.service.NoticeService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 사용자가 공지사항을 조회한다.
     * @param googleId 사용자의 구글 아이디
     * @param type 공지사항 종류
     * @param page 조회할 페이지 번호
     * @return 페이지 정보 및 공지사항들
     */
    @GetMapping()
    ApiResponse getNotices(@RequestHeader(value = "googleId", required = false) String googleId,
                           @RequestParam("type")String type, @RequestParam("page") int page){
        if (googleId == null){ // 로그인 하지 않은 사용자
            try {
                Page<NoticeResponse> findNoticesWithPaging = noticeService.getNoticesWithoutLogin(
                        NoticeType.matchWithLowerCase(type), page);
                return getApiResponse(findNoticesWithPaging);
            }
            catch (ValidationException e) {
                log.error(e.toString());
                return ErrorResponse.error(ErrorCode.VALIDATION_REQUEST_MISSING_EXCEPTION);
            } catch (Exception e) {
                log.error(e.toString());
                return ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }else { // 로그인 한 사용자
            try {
                Page<NoticeResponse> findNoticesWithPaging = noticeService.getNoticesWithLogin(
                        googleId, NoticeType.matchWithLowerCase(type), page);
                return getApiResponse(findNoticesWithPaging);
            }
            catch (ValidationException e) {
                log.error(e.toString());
                return ErrorResponse.error(ErrorCode.VALIDATION_REQUEST_MISSING_EXCEPTION);
            } catch (Exception e) {
                log.error(e.toString());
                return ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }
    }

    /**
     * API 응답 생성
     * @param findNoticesWithPaging 페이지 정보가 담긴 공지사항들
     * @return API 응답
     */
    @NotNull
    private ApiResponse getApiResponse(Page<NoticeResponse> findNoticesWithPaging) {
        if(findNoticesWithPaging.isEmpty()) throw new ValidationException();
        Map<String, Object> response = new HashMap<>();
        response.put("notices", findNoticesWithPaging.getContent());
        response.put("page", findNoticesWithPaging.getNumber() + 1);
        response.put("totalPages", findNoticesWithPaging.getTotalPages());
        return SuccessResponse.success(SuccessCode.GET_NOTICES_WITHOUT_LOGIN, response);
    }
}
