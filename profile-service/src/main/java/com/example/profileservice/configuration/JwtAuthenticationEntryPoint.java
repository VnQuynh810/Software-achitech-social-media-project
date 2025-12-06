//package com.example.profileservice.configuration;
//
//
//import com.example.profileservice.dto.responses.ApiResponse;
//import com.example.profileservice.exception.ErrorCode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(
//            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//            throws IOException {
//        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
//
//        response.setStatus(errorCode.getStatusCode().value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        ApiResponse<?> apiResponse = ApiResponse.builder()
//                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
//        response.flushBuffer();
//    }
//}
