package com.example.notifyserver.scrap.controller;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.dto.ErrorResponse;
import com.example.notifyserver.common.dto.Response;
import com.example.notifyserver.common.dto.SuccessNonDataResponse;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import com.example.notifyserver.common.exception.enums.SuccessCode;
import com.example.notifyserver.common.exception.model.NotFoundException;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.common.repository.NoticeRepository;
import com.example.notifyserver.scrap.dto.DeleteScrapRequest;
import com.example.notifyserver.scrap.dto.SaveScrapRequest;
import com.example.notifyserver.scrap.service.ScrapService;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final NoticeRepository<Notice, Long> noticeRepository;
    private final UserRepository userRepository;

    /**
     * 스크랩을 저장
     * @param request 사용자로부터 받은 RequestBody
     * @return 성공 코드
     */
    @PostMapping
    public SuccessNonDataResponse saveScrap(@RequestBody SaveScrapRequest request){

        User findUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        Notice findNotice = noticeRepository.findById(request.noticeId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));

        try {
            scrapService.doScrap(findUser, findNotice);
        }catch (Exception e) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
        }

        return SuccessNonDataResponse.success(SuccessCode.SAVE_SCRAP_SUCCESS);
    }

    /**
     * 스크랩을 제거
     * @param request 사용자로부터 받은 RequestBody
     * @return 성공 코드
     */
    @DeleteMapping
    public Response deleteScrap(@RequestBody DeleteScrapRequest request){

        User findUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        Notice findNotice = noticeRepository.findById(request.noticeId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));

        try {
            scrapService.deleteScrap(findUser, findNotice);
            return SuccessNonDataResponse.success(SuccessCode.DELETE_SCRAP_SUCCESS);
        }catch (Exception e) {
            return ErrorResponse.error(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
        }
    }
}
