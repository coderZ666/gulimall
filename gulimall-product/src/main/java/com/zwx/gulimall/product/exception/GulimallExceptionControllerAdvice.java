package com.zwx.gulimall.product.exception;

import com.zwx.common.exception.BizCodeEnum;
import com.zwx.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有controller异常
 * @author coderZWX
 * @date 2021-01-05 18:24
 */

//@ResponseBody
//@ControllerAdvice(basePackages = "com.zwx.gulimall.product.controller")
@Slf4j
@RestControllerAdvice(basePackages = "com.zwx.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    /**
     * 处理数据校验异常
     * @param e 异常对象
     * @return 异常信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据校验出现异常：{},异常类型：{}",e.getMessage(),e.getClass());
        //获取数据校验错误的详细信息
        BindingResult result = e.getBindingResult();
        //遍历结果拿到需要的异常信息封装进map返回
        Map<String,String> errorMap = new HashMap<>();
        result.getFieldErrors().forEach((item)->{
            //FieldError 获取到错误提示
            String message = item.getDefaultMessage();
            //获取错误的属性名字
            String field = item.getField();
            errorMap.put(field,message);
        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(),BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("data",errorMap);
    }

    /**
     * 所有没精确匹配的位置异常通用处理
     * @param throwable 异常对象
     * @return 异常信息
     */
//    @ExceptionHandler(value = Throwable.class)
//    public R handleException(Throwable throwable) {
//        log.error("未知异常：{}",throwable.getClass());
//        log.error("异常信息：{}",throwable.getMessage());
//        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(),BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
//    }

}

