package com.example.notifyserver.user.controller;

import com.example.notifyserver.common.dto.ApiResponse;
import com.example.notifyserver.common.dto.ErrorResponse;
import com.example.notifyserver.common.dto.SuccessNonDataResponse;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import com.example.notifyserver.common.exception.enums.SuccessCode;
import com.example.notifyserver.common.exception.model.NotFoundUserException;
import com.example.notifyserver.user.dto.request.LoginRequest;
import com.example.notifyserver.user.dto.request.RegisterRequest;
import com.example.notifyserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mem")
@CrossOrigin(origins = "*", methods = RequestMethod.POST)
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse userLogin(@RequestBody LoginRequest request) {
        try {
            userService.userLogin(request);
            return SuccessNonDataResponse.success(SuccessCode.LOGIN_SUCCESS);
        } catch (NotFoundUserException e) {
            return ErrorResponse.error(ErrorCode.USER_NOT_FOUND_EXCEPTION);
        } catch (Exception e) {
            return ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    @PostMapping("/register")
    public ApiResponse userRegister(@RequestHeader("googleId") String googleId, @RequestBody RegisterRequest request){
        try {
            userService.userRegister(request, googleId);
            return SuccessNonDataResponse.success(SuccessCode.REGISTER_SUCCESS);
        } catch (NotFoundUserException e) {
            return ErrorResponse.error(ErrorCode.USER_NOT_FOUND_EXCEPTION);
        } catch (Exception e) {
            return ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }

    }
}

