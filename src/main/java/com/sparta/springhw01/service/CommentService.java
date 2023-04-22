package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.CommentRequestDto;
import com.sparta.springhw01.dto.CommentResponseDto;
import com.sparta.springhw01.entity.Comment;
import com.sparta.springhw01.entity.Post;
import com.sparta.springhw01.entity.User;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.CommentRepository;
import com.sparta.springhw01.repository.PostRepository;
import com.sparta.springhw01.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;



@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    //댓글 작성하기
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        /*
        * - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
          - 선택한 게시글의 DB 저장 유무를 확인하기
          - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
        * */

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
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

            // 선택한 게시글의 DB 저장 유무를 확인하기
            Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, post, user));

            return new CommentResponseDto(comment);
        } else {
            return null;
        }


    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        /*
        * - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
          - 선택한 댓글의 DB 저장 유무를 확인하기
          - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
        * */

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 수정 가능
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

//            // 선택한 댓글의 게시글 삭제 유무 확인하기
//            Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
//                    () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
//            );

            // 댓글 존재 유무 && 작성자 확인
            Comment comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않거나 작성자가 불일치 합니다.")
            );

            comment.update(requestDto);

            return new CommentResponseDto(comment);

        } else {
            return null;
        }
    }

    public boolean deleteComment(Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰 검사
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

            Comment comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            commentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
