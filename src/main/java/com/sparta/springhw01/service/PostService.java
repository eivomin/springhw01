package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.PostRequestDto;
import com.sparta.springhw01.dto.PostResponseDto;
import com.sparta.springhw01.entity.Post;
import com.sparta.springhw01.entity.PostLike;
import com.sparta.springhw01.entity.User;
import com.sparta.springhw01.entity.UserRoleEnum;
import com.sparta.springhw01.exception.ApiException;
import com.sparta.springhw01.exception.ExceptionEnum;
import com.sparta.springhw01.jwt.JwtUtil;
import com.sparta.springhw01.repository.PostLikeRepository;
import com.sparta.springhw01.repository.PostRepository;
import com.sparta.springhw01.repository.UserRepository;
import com.sparta.springhw01.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final JwtUtil jwtUtil;

    /* 게시글 작성 */
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
            Post post = postRepository.saveAndFlush(new Post(requestDto, user));
            return new PostResponseDto(post);
    }

    /* 게시글 전체 조회 */
    public List<PostResponseDto> getPosts() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Post post : postList){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }

    /* 게시글 수정
    * ADMIN 회원은 모든 게시글 수정 가능
    *  */
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto requestDto, User user) {
            Post post = new Post();
            // 관리자 검증 여부
            if(user.getRole().name().equals("ADMIN")){
                // ROLE == ADMIN
                post = postRepository.findById(id).orElseThrow(
                        () -> new ApiException(ExceptionEnum.INVALID_POST_EXCEPTION)
                );
            }else{
                // ROLE == USER
                post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                        () -> new ApiException(ExceptionEnum.UNAUTHORIZED_EXCEPTION)
                );
            }
            post.update(requestDto);
            return new PostResponseDto(post);
    }

    /* 게시글 삭제
     * ADMIN 회원은 모든 게시글 삭제 가능
     *  */
    @Transactional
    public void deletePost(Long id, User user) {
            Post post = new Post();

            // 관리자 검증 여부
            if(user.getRole().equals(UserRoleEnum.ADMIN)){
                // ROLE == ADMIN
                post = postRepository.findById(id).orElseThrow(
                        () -> new ApiException(ExceptionEnum.INVALID_POST_EXCEPTION)
                );
            }else{
                // ROLE == USER
                post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                        () -> new ApiException(ExceptionEnum.UNAUTHORIZED_EXCEPTION)
                );
            }
            postRepository.deleteById(id);
    }

    /* 게시글 상세 조회 */
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () ->  new ApiException(ExceptionEnum.INVALID_POST_EXCEPTION)
        );
        return new PostResponseDto(post);
    }

    /* 게시글 좋아요 */
    @Transactional
    public boolean saveLikes(Long id, User user) {
        // 1. 해당 게시글 존재 여부
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApiException(ExceptionEnum.INVALID_POST_EXCEPTION)
        );

        //user 좋아요 이력 확인
        Optional<PostLike> findPostLike = postLikeRepository.findByPostIdAndUserId(id, user.getId());

        if(findPostLike.isEmpty()) {
            postLikeRepository.save(new PostLike(post, user));
            post.addLike();
            return true;
        }
        return false;
    }

    /* 게시글 싫어요 */
    @Transactional
    public boolean deleteLikes(Long id, User user) {
        // 1. 해당 게시글 존재 여부
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApiException(ExceptionEnum.INVALID_POST_EXCEPTION)
        );
        //user 좋아요 이력 확인
        Optional<PostLike> findPostLike = postLikeRepository.findByPostIdAndUserId(id, user.getId());

        if(findPostLike.isPresent()) {
            postLikeRepository.deleteById(findPostLike.get().getId());
            post.deleteLike();
            return true;
        }
        return false;
    }

}
