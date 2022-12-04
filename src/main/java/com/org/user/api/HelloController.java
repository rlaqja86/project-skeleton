package com.org.user.api;

import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/hello")
public class HelloController extends BaseController {

    @GetMapping
    public ResponseVo<String> hello() {
        return new ResponseVo<>("hello", ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}
