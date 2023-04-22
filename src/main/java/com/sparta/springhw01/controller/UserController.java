package com.sparta.springhw01.controller;

import com.sparta.springhw01.dto.*;
import com.sparta.springhw01.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    /* 회원가입 api */
    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        StatusResponseDto res = new StatusResponseDto(
                200,
                HttpStatus.OK,
                "회원 가입 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
        //return "redirect:/api/user/login";
    }

    /* 로그인 api */
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<StatusResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        StatusResponseDto res = new StatusResponseDto(
                200,
                HttpStatus.OK,
                "로그인 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

}