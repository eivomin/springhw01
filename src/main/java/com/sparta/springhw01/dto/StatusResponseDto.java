package com.sparta.springhw01.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class StatusResponseDto {
    private int code;
    private HttpStatus httpStatus;
    private String message;
}
