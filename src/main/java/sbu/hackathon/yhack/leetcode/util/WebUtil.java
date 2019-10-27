package sbu.hackathon.yhack.leetcode.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import sbu.hackathon.yhack.leetcode.exceptions.ControllerException;
import sbu.hackathon.yhack.leetcode.exceptions.ServiceException;
import sbu.hackathon.yhack.leetcode.model.APIResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public final class WebUtil {

    public static final String API_START_TIME = "API_START_TIME";

    public static final String HEADER_NAME_USER_AGENT = "User-Agent";

    public static final String HEADER_REQUEST_ID = "X-REQUEST-ID";

    public static final String HEADER_DEBUG = "debug";

    private WebUtil() {

    }

    public static APIResponse getResponseFromException(Exception ex, Map<String, Map<String, String>> detailMessage, MessageSource messageSource, HttpServletRequest request, APIResponse apiResponse) {

        if (ex instanceof ServiceException) {

            apiResponse.setCode(((ServiceException) ex).getErrorCode());
            apiResponse.setMessage(ex.getMessage());
            return apiResponse;
        }
        if (ex instanceof ResponseStatusException) {
            apiResponse.setCode(((ResponseStatusException) ex).getStatus().value());
            apiResponse.setMessage(ex.getMessage());
            return apiResponse;
        }

        if (ex instanceof ControllerException) {
            apiResponse.setCode(((ControllerException) ex).getResponseCode());
            populateExceptionDetails((ControllerException) ex, detailMessage, messageSource, request);
            return apiResponse;
        }
        if (ex instanceof HttpMessageNotReadableException) {
            apiResponse.setCode(400);
            apiResponse.setMessage(ex.getMessage());
            return apiResponse;
        }
        return getResponseCodeEnumForMiscException(ex, apiResponse);
    }

    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
        if (!(reqAttr instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) reqAttr;
        return servletReqAttr.getRequest();
    }

    public static String getHeader(String headerName) {
        RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
        if (!(reqAttr instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) reqAttr;
        return servletReqAttr.getRequest().getHeader(headerName);
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public static String getRequestDetailsForLog(HttpServletRequest request, String delimiter) {

        StringBuilder sb = new StringBuilder();
        sb.append("Request = [" + request.getMethod() + " " + request.getRequestURL() + "]" + delimiter);
        sb.append("Remote address = [" + request.getRemoteAddr() + "]" + delimiter);
        sb.append("Remote host = [" + request.getRemoteHost() + "]" + delimiter);
        sb.append("Remote port = [" + request.getRemotePort() + "]" + delimiter);
        sb.append("Remote user = [" + request.getRemoteUser() + "]" + delimiter);
        sb.append("Auth type = [" + request.getAuthType() + "]" + delimiter);
        String headers = Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)))
                .collect(Collectors.joining(delimiter));

        if (headers.isEmpty()) {
            sb.append("Headers : NONE" + delimiter);
        } else {
            sb.append("Headers: [" + headers + "]" + delimiter);
        }

        String parameters = Collections.list(request.getParameterNames()).stream()
                .map(p -> p + " : " + Arrays.asList(request.getParameterValues(p)))
                .collect(Collectors.joining(delimiter));

        if (parameters.isEmpty()) {
            sb.append("Parameters: NONE" + delimiter);
        } else {
            sb.append("Parameters: [" + parameters + "]" + delimiter);
        }
        return sb.toString();
    }

    public static String getRequestURI() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return null;
        }
        return request.getRequestURI().replaceFirst(request.getContextPath(), "");
    }

    private static APIResponse getResponseCodeEnumForMiscException(Exception ex, APIResponse apiResponse) {
        if (ex instanceof HttpMessageNotReadableException || ex instanceof MethodArgumentTypeMismatchException
                || ex instanceof BindException || ex instanceof ConstraintViolationException) {
            apiResponse.setCode(400);
            return apiResponse;
        }
        apiResponse.setCode(500);
        apiResponse.setMessage(ex.getLocalizedMessage());
        return apiResponse;

    }

    private static void populateExceptionDetails(ControllerException ce, Map<String, Map<String, String>> detailMessage,
                                                 MessageSource messageSource, HttpServletRequest request) {
        if (ce.getBindingErrors() != null && !ce.getBindingErrors().getFieldErrors().isEmpty()) {
            Locale locale = request.getLocale();
            for (FieldError fieldError : ce.getBindingErrors().getFieldErrors()) {
                if (fieldError == null) continue;
                String message = fieldError.getDefaultMessage();
                if (message == null) {
                    String code = fieldError.getCode() == null ? "CODE_NOT_FOUND" : fieldError.getCode();
                    message = messageSource.getMessage(code, fieldError.getArguments(), ":::-" + code + "-:::", locale);
                }
                Map<String, String> msg = new HashMap<>();
                msg.put("message", message);
                detailMessage.put(fieldError.getField(), msg);
            }
        }
    }
}
