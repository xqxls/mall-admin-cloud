package com.xqxls.cloud.exception;

import com.xqxls.cloud.api.CommonResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 * Created by xqxls on 2020/7/17.
 */
@ControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public CommonResult<Object> handleOauth2(OAuth2Exception e) {
        return CommonResult.failed(e.getMessage());
    }
}
