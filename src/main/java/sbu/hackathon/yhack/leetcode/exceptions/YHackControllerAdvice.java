
package sbu.hackathon.yhack.leetcode.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sbu.hackathon.yhack.leetcode.model.APIResponse;
import sbu.hackathon.yhack.leetcode.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@ControllerAdvice("sbu.hackathon")
public class YHackControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        String requestURI = request.getRequestURI();
        requestURI = request.getMethod() + ":" + requestURI.replaceAll("[\n|\r|\t]", "_");

        log.error("Exception occurred while executing: " + requestURI, ex);
        APIResponse apiResponse = new APIResponse<>();
        Map<String, Map<String, String>> detailMessage = apiResponse.getDetailMessage();
        apiResponse = WebUtil.getResponseFromException(ex, detailMessage, messageSource, request, apiResponse);

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getCode()));
    }

}
