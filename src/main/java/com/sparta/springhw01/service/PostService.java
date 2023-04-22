package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.PostRequestDto;
import com.sparta.springhw01.dto.PostResponseDto;
import com.sparta.springhw01.entity.Post;
import com.sparta.springhw01.entity.User;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.PostRepository;
import com.sparta.springhw01.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 등록 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Post post = postRepository.saveAndFlush(new Post(requestDto, user));

            return new PostResponseDto(post);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Post post : postList){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }

    @Transactional
    public PostResponseDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 수정 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new NullPointerException("해당 게시글은 존재하지 않습니다.")
            );

            post.update(requestDto);

            return new PostResponseDto(post);

        } else {
            return null;
        }
    }

    @Transactional
    public boolean deletePost(Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new NullPointerException("해당 게시글은 존재하지 않습니다.")
            );

            postRepository.deleteById(id);
            return true;
        } else {
            return false;
        }

    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () ->  new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        return new PostResponseDto(post);
    }
}
