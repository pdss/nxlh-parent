package com.nxlh.manager.exception;

import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.MyException;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {


    // 捕捉自定义异常
    @ExceptionHandler(Exception.class)
    public void customeException(HttpServletRequest request, Throwable ex) {
        this.log.error("定时更新异常", ex);
        ex.printStackTrace();
        this.log.error(ex.toString());
    }


}