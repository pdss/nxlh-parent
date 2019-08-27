package com.nxlh.minapp.exception;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.MyException;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.service.MqFailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

//    @Reference
    //private MqFailService mqFailService;


    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    public MyResult globalException(HttpServletRequest request, Throwable ex) {
        this.log.error("发生异常", ex);
//        this.mqFailService.send(ex.toString());
        return MyResult.build(HttpResponseEnums.InternalServerError, null);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResult requestParametersException(HttpServletRequest request, Throwable ex) {
        this.log.error("请求参数无效", ex);
//        this.mqFailService.send(ex.toString());
        return MyResult.build(HttpResponseEnums.InternalServerError, null);
    }

    // 捕捉自定义异常
    @ExceptionHandler(MyException.class)
    public MyResult customeException(HttpServletRequest request, Throwable ex) {
        this.log.error("自定义异常", ex);
//        this.mqFailService.send(ex.toString());
        return MyResult.build(HttpResponseEnums.InternalServerError, null);
    }

    /**
     * 未授权
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(UnAuthorizeException.class)
    public MyResult unAuthorizeException(HttpServletRequest request,Throwable ex){
        this.log.error("未授权,请重新登录",ex);
        return MyResult.build(HttpResponseEnums.UnAuthorized, null);

    }


}