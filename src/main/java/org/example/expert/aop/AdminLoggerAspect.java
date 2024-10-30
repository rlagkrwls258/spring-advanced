package org.example.expert.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AdminLoggerAspect {

    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    public void adminDeleteComment() {}

    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void adminChangeUserRole() {}

    @Around("adminChangeUserRole() || adminDeleteComment()")
    public Object logAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        // 접근 로그 기록
        log.info("-----------------\n관리자 접근 시도");
        log.info("사용자 ID = {}",(String) request.getAttribute("userId"));
        log.info("요청 시간 = {}", LocalDateTime.now());
        log.info("요청 URL = {}", request.getRequestURI());
        log.info("요청 본문 = {}", Arrays.toString(joinPoint.getArgs()));


        // 원래 메서드 실행
        Object response = joinPoint.proceed();

        // 응답 결과 로그 기록
        log.info("응답: {}", response);

        return response;
    }
}
