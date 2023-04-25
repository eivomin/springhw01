package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.LoginRequestDto;
import com.sparta.springhw01.dto.SignupRequestDto;
import com.sparta.springhw01.entity.User;
import com.sparta.springhw01.entity.UserRoleEnum;
import com.sparta.springhw01.exception.ApiException;
import com.sparta.springhw01.exception.ExceptionEnum;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    /* 회원가입 */
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        // encoding 하기 전 password 검사
        if(isValidPassword(password)){
            password = passwordEncoder.encode(password);
        }else{
            throw new ApiException(ExceptionEnum.PASSWORD_EXCEPTION);
        }

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

    //최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
    private boolean isValidPassword(String password) {
        String pattern = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
        if (Pattern.matches(pattern, password)) return true;
        else return false;

    }

    /* 로그인 */
    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApiException(ExceptionEnum.INVALID_USER_EXCEPTION)
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new ApiException(ExceptionEnum.ID_PASSWORD_EXCEPTION);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}