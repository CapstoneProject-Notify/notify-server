package com.example.notifyserver.scrap.controller;

import com.example.notifyserver.common.constants.NoticeConstants;
import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.dto.ErrorResponse;
import com.example.notifyserver.common.dto.Response;
import com.example.notifyserver.common.dto.SuccessNonDataResponse;
import com.example.notifyserver.common.dto.SuccessResponse;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import com.example.notifyserver.common.exception.enums.SuccessCode;
import com.example.notifyserver.common.exception.model.NotFoundException;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.common.repository.NoticeRepository;
import com.example.notifyserver.scrap.domain.Scrap;
import com.example.notifyserver.scrap.dto.DeleteScrapRequest;
import com.example.notifyserver.scrap.dto.GetScrapsResponse;
import com.example.notifyserver.scrap.dto.NoticeResponse;
import com.example.notifyserver.scrap.dto.SaveScrapRequest;
import com.example.notifyserver.scrap.service.ScrapService;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
@Slf4j
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

        User findUser = userRepository.findUserByGoogleId(request.googleId())
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

        User findUser = userRepository.findUserByGoogleId(request.googleId())
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

    /**
     * 스크랩을 조회
     * @param googleId 스크랩을 조회할 사용자의 구글 아이디
     * @param pageNum 스크랩을 조회할 페이지 숫자
     * @return 성공 코드, 스크랩 정보 , 페이지 정보
     */
    @GetMapping
    public Response getScraps(@RequestHeader("googleId") String googleId, @RequestParam("page") long pageNum){

        User findUser = userRepository.findUserByGoogleId(googleId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        PageRequest pageRequest = PageRequest.of((int) pageNum, (int) NoticeConstants.PAGE_SIZE);

        try {
            Page<Scrap> result = scrapService.getScrap(findUser.getUserId(), pageRequest);
            List<Scrap> scrapList = result.getContent();
            GetScrapsResponse response = GetScrapsResponse.builder()
                    .totalPages(result.getTotalPages())
                    .page(result.getNumber())
                    .notices(scrapToNoticeResponse(scrapList))
                    .build();
            return SuccessResponse.success(SuccessCode.GET_SCRAP_SUCCESS,response);
        }catch (Exception e) {
            return ErrorResponse.error(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
        }
    }

    /**
     * DB에서 가져온 스크랩을 API 형식에 맞게 변환해주는 메서드
     * @param scrapList DB에서 가져온 스크랩
     * @return API 요구 형식
     */
    private static List<NoticeResponse> scrapToNoticeResponse(List<Scrap> scrapList) {
        List<NoticeResponse> list = new ArrayList<>();
        for(Scrap s : scrapList){
            list.add(
                    NoticeResponse.builder()
                            .scrapId(s.getScrapId())
                            .title(s.getNotice().getNoticeTitle())
                            .url(s.getNotice().getNoticeUrl())
                            .isScrapped(true)
                            .build()
            );
        }
        return list;
    }

}
