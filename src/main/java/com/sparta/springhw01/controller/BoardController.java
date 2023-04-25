package com.sparta.springhw01.controller;

import com.sparta.springhw01.entity.User;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.UserRepository;
//import com.sparta.springhw01.service.FolderService;
import com.sparta.springhw01.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    //private final FolderService folderService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/board")
    public ModelAndView board() {
        return new ModelAndView("index");
    }

// 로그인 한 유저가 메인페이지를 요청할 때 유저의 이름 반환
    @GetMapping("/user-info")
    @ResponseBody
    public String getUserName(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }
}
