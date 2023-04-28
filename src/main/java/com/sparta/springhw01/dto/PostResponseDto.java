package com.sparta.springhw01.dto;

import com.sparta.springhw01.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성해줌
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String username;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long likeCount;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.username = post.getUser().getUsername();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCount = post.getLikeCount();
    }
}
