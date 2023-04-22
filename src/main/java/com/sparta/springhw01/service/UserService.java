package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.LoginRequestDto;
import com.sparta.springhw01.dto.SignupRequestDto;
import com.sparta.springhw01.dto.StatusResponseDto;
import com.sparta.springhw01.entity.User;
import com.sparta.springhw01.entity.UserRoleEnum;
import com.sparta.springhw01.exception.ApiException;
import com.sparta.springhw01.exception.ExceptionEnum;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    /* 회원가입 */
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new ApiException(ExceptionEnum.DUPLICATE_EXCEPTION);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new ApiException(ExceptionEnum.ADMIN_TOKEN_EXCEPTION);
            }
            role = UserRoleEnum.ADMIN;
        }else role = UserRoleEnum.USER;

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    /* 로그인 */
    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        Optional<User> user = userRepository.findByUsername(username);

        // 사용자 존재
        if(!user.isPresent()) {
            throw new ApiException(ExceptionEnum.INVALID_USER_EXCEPTION);
        }

        // 사용자는 존재하나 비밀번호랑 다를 때
        if(!user.get().getPassword().equals(password)) {
            throw new ApiException(ExceptionEnum.PASSWORD_EXCEPTION);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
    }
}