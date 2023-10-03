package com.example.kakao._core.advice;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.kakao._core.errors.exception.Exception400;

@Aspect // AOP 클래스 생성
@Component // IOC 등록
public class ValidAdvice {

    // 1. 별칭 등록
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    // 2. 공통 기능
    @Before("postMapping()") // postMapping이 실행 되기 전 aop 실행
    public void checkValid(JoinPoint jp) {
        Object[] args = jp.getArgs(); // 해당 메소드의 매개변수 목록 담기
        for (Object arg : args) {
            if (arg instanceof Errors) { // 매개변수의 타입이 Errors라면,
                Errors errors = (Errors) arg; // 그 매개변수를 errors에 담기

                if (errors.hasErrors()) { // errors에 오류가 있다면,
                    List<FieldError> fieldErrors = errors.getFieldErrors(); // 필드에 대한 오류정보를 담기
                    throw new Exception400(
                            fieldErrors.get(0).getDefaultMessage() + " : " + fieldErrors.get(0).getField());
                    // fielderrors에서 가져온 오류 메세지 : 유효성 검사 오류가 발생한 필드의 이름
                }
            }
        }
    }
}
