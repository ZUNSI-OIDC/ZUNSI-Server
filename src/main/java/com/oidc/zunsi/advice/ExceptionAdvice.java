package com.oidc.zunsi.advice;

import com.oidc.zunsi.domain.response.CommonResult;
import com.oidc.zunsi.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return responseService.getFailResult("unknown error");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult illegalArgumentException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return responseService.getFailResult(e.getMessage());
    }
}
