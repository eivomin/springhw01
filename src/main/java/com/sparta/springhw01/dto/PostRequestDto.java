package com.sparta.springhw01.dto;

import com.sparta.springhw01.entity.User;
import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String contents;
}
