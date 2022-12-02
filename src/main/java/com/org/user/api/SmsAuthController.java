package com.org.user.api;

import com.org.user.application.SmsApplication;
import com.org.user.model.dto.SignUpDto;
import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/sms")
@AllArgsConstructor
public class SmsAuthController extends BaseController {

    private final SmsApplication smsApplication;

    @PostMapping
    public ResponseVo<SignUpDto> post(@RequestParam String phoneNumber) {
        SignUpDto signUpDto = smsApplication.sendSMSAuthCode(phoneNumber);
        return new ResponseVo<>(signUpDto,
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/code")
    public ResponseVo<SignUpDto> authPhoneNumber(@RequestBody SignUpDto signUpDto) {
        //match code correct from send by SMS
        return new ResponseVo<>(smsApplication.updateNextUrl(signUpDto),
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage());
    }
}
