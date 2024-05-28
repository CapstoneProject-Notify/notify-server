package com.example.notifyserver.notice.controller;

import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.common.dto.ApiResponse;
import com.example.notifyserver.common.dto.ErrorResponse;
import com.example.notifyserver.common.dto.SuccessResponse;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import com.example.notifyserver.common.exception.enums.SuccessCode;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.notice.dto.NoticeResponse;
import com.example.notifyserver.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 로그인 하지 않은 사용자가 공지사항을 조회한다.
     * @param type 공지사항 종류
     * @param page 조회할 페이지 번호
     * @return 페이지 정보 및 공지사항들
     */
    @GetMapping()
    ApiResponse getNoticesWithoutLogin(@RequestParam("type")String type, @RequestParam("page") int page){
        try {
            Page<NoticeResponse> findNoticesWithPaging = noticeService.getNoticesWithoutLogin(
                    NoticeType.matchWithLowerCase(type), page);
            Map<String, Object> response = new HashMap<>();
            response.put("notices", findNoticesWithPaging.getContent());
            response.put("page", findNoticesWithPaging.getNumber() + 1);
            response.put("totalPages", findNoticesWithPaging.getTotalPages());
            return SuccessResponse.success(SuccessCode.GET_NOTICES_WITHOUT_LOGIN, response);
        }
        catch (NotFoundUserException e) {
            return ErrorResponse.error(ErrorCode.VALIDATION_REQUEST_MISSING_EXCEPTION);
        } catch (Exception e) {
            return ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }

    }
}
