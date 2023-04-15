package com.sparta.springhw01.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String password;
    private String title;
    private String writer;
    private String contents;
}
