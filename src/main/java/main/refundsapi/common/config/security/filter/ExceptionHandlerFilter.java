package main.refundsapi.common.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common.CommonEnum;
import main.refundsapi.common.ErrorCode;
import main.refundsapi.common.exception.JwtException;
import main.refundsapi.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        }catch (JwtException e){
            setErrorResponse(response, e.getHttpStatus(), new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), e.getCode(), e.getMessage()));
        }catch (AuthenticationCredentialsNotFoundException e){
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage()));
        }
    }
    private void setErrorResponse(
            HttpServletResponse response,
            HttpStatus httpStatus,
            ErrorResponse errorResponse
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            log.error("ExceptionHandlerFilter Error : {}", e.getMessage());
        }
    }
}
