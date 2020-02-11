package com.dszj.manage.exception;

import java.util.UUID;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dszj.manage.base.ResultEnum;
import com.dszj.manage.base.ResultVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 客服服务全局异常处理
 * @author yys
 *
 */
@SuppressWarnings("rawtypes")
@ControllerAdvice
@Slf4j
public class BizExceptionResolver {

    @ExceptionHandler(value = BizException.class)
    public @ResponseBody ResultVO bizExceptionHandler(BizException e) {
    	String traceId = UUID.randomUUID().toString();
		ResultVO resultVO = new ResultVO();
		resultVO.setCode(e.getCode());
		resultVO.setMsg(e.getMessage());
		resultVO.setErrDesc(e.getErrorDesc());
		resultVO.setTraceId(traceId);
		log.error(traceId,e);
        return resultVO;
    }

    @ExceptionHandler(value = Exception.class)
    public @ResponseBody ResultVO exceptionHandler(Exception e) {
    	String traceId = UUID.randomUUID().toString();
		ResultVO resultVO = new ResultVO();
		resultVO.setCode(ResultEnum.SYS_ERROR.getCode());
		resultVO.setMsg(ResultEnum.SYS_ERROR.getMsg());
		resultVO.setErrDesc(e.getMessage());
		resultVO.setTraceId(traceId);
		log.error(traceId,e);
        return resultVO;
    }
    
}