package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.CommentRequestDto;
import com.sparta.springhw01.dto.CommentResponseDto;
import com.sparta.springhw01.entity.*;
import com.sparta.springhw01.exception.ApiException;
import com.sparta.springhw01.exception.ExceptionEnum;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.CommentLikeRepository;
import com.sparta.springhw01.repository.CommentRepository;
import com.sparta.springhw01.repository.PostRepository;
import com.sparta.springhw01.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtil jwtUtil;

    /* 댓글 작성 */
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        /*
        * - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
          - 선택한 게시글의 DB 저장 유무를 확인하기
          - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
        * */
            // 선택한 게시글의 DB 저장 유무를 확인하기
            Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                    () -> new ApiException(ExceptionEnum.INVALID_POST_EXCEPTION)
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, post, user));

            return new CommentResponseDto(comment);
    }

    /* 댓글 수정
     * ADMIN 회원은 모든 댓굴 수정 가능
     *  */
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        /*
        * - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
          - 선택한 댓글의 DB 저장 유무를 확인하기
          - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
        * */
            Comment comment = new Comment();

            // 관리자 검증 여부
            if(user.getRole().equals(UserRoleEnum.ADMIN)){
                // ROLE == ADMIN
                comment = commentRepository.findById(id).orElseThrow(
                        () -> new ApiException(ExceptionEnum.INVALID_COMMENT_EXCEPTION)
                );
            }else{
                // ROLE == USER
                comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                        () -> new ApiException(ExceptionEnum.UNAUTHORIZED_EXCEPTION)
                );
            }

            comment.update(requestDto);

            return new CommentResponseDto(comment);

    }

    /* 댓글 삭제
     * ADMIN 회원은 모든 댓굴 수정 가능
     *  */
    @Transactional
    public void deleteComment(Long id, User user) {
            Comment comment = new Comment();

            // 관리자 검증 여부
            if(user.getRole().name().equals("ADMIN")){
                // ROLE == ADMIN
                comment = commentRepository.findById(id).orElseThrow(
                        () -> new ApiException(ExceptionEnum.INVALID_COMMENT_EXCEPTION)
                );
            }else{
                // ROLE == USER
                comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                        () -> new ApiException(ExceptionEnum.UNAUTHORIZED_EXCEPTION)
                );
            }

            commentRepository.deleteById(id);

    }

    /* 댓글 좋아요 */
    @Transactional
    public boolean saveLikes(Long id, User user) {
        // 1. 해당 댓글 존재 여부
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ApiException(ExceptionEnum.INVALID_COMMENT_EXCEPTION)
        );

        //user 좋아요 이력 확인
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUserId(id, user.getId());

        if(findCommentLike.isEmpty()) {
            commentLikeRepository.save(new CommentLike(user, comment));
            comment.addLike();
            return true;
        }
        return false;
    }

    /* 댓글 싫어요 */
    @Transactional
    public boolean deleteLikes(Long id, User user) {
        // 1. 해당 댓글 존재 여부
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ApiException(ExceptionEnum.INVALID_COMMENT_EXCEPTION)
        );

        //user 좋아요 이력 확인
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUserId(id, user.getId());

        if(findCommentLike.isPresent()) {
            commentLikeRepository.deleteById(findCommentLike.get().getId());
            comment.deleteLike();
            return true;
        }
        return false;
    }
}
