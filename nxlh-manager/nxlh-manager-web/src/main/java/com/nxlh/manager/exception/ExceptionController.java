package com.nxlh.manager.exception;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.model.dto.LogDTO;
import com.nxlh.manager.service.LogService;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @Reference
    private MqFailService mqFailService;

    // 捕捉shiro的异常
    @ExceptionHandler(ShiroException.class)
    public MyResult handle401(ShiroException e) {
        return MyResult.build(HttpResponseEnums.UnAuthorized, null);
    }

    // 捕捉UnauthorizedException
    @ExceptionHandler(UnauthorizedException.class)
    public MyResult handle401() {
        return MyResult.build(HttpResponseEnums.UnAuthorized, null);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    public MyResult globalException(HttpServletRequest request, Throwable ex) {
        this.log.error("发生异常", ex);


        return MyResult.build(HttpResponseEnums.InternalServerError, null);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResult requestParametersException(HttpServletRequest request, Throwable ex) {
        this.log.error("请求参数无效", ex);
        this.mqFailService.send(ex.toString());
        return MyResult.build(HttpResponseEnums.InternalServerError, null);
    }


    /**
     * 文件上传大小限制
     *
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public MyResult uploadException(HttpServletRequest request, Throwable ex) {
        this.log.error("文件上传错误,文件太大");
        var map = new HashMap<String, String>();
        map.put("error", "文件过大");
        this.mqFailService.send(ex.toString());
        return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "文件太大，请上传1Mb以内文件", map);
    }


}