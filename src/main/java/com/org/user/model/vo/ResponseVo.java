package com.org.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseVo<T> {
    private T data;
    private int statusCode;
    private String message;
}
