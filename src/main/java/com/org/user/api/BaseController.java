package com.org.user.api;

import com.org.user.exception.SignUpException;
import com.org.user.exception.SmsException;
import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @ExceptionHandler({RuntimeException.class})
    public ResponseVo<?> defaultExceptionHandler(Exception exception) {
            return new ResponseVo<>(ResponseCode.FAILED_UNKNOWN.getMessage(), ResponseCode.FAILED_UNKNOWN.getCode(), exception.getMessage());
    }

    @ExceptionHandler({SignUpException.class})
    public ResponseVo<?> signUpExceptionHandler(Exception exception) {
        return new ResponseVo<>(ResponseCode.FAILED_SIGN_UP.getMessage(), ResponseCode.FAILED_SIGN_UP.getCode(), exception.getMessage());
    }

    @ExceptionHandler({SmsException.class})
    public ResponseVo<?> smsExceptionHandler(Exception exception) {
        return new ResponseVo<>(ResponseCode.FAILED_SIGN_UP.getMessage(), ResponseCode.FAILED_SIGN_UP.getCode(), exception.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseVo<?> badCredentialExceptionHandler(Exception exception) {
        return new ResponseVo<>(ResponseCode.FAILED_SIGN_UP.getMessage(), ResponseCode.FAILED_LOGIN.getCode(), exception.getMessage());
    }
}
