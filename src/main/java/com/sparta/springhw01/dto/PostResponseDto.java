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

    // Entity -> Dto로 변환
    /*
     * Entity -> Dto 변환 이유
     * 만약에 Dto가 없이 Entity만 존재한다고 가정하면 DB와 직접적으로 매핑되어있는 Entity가 변경되면 여러 클래스에 영향을 미치게 될것이다.
     * 하지만 Dto가 존재한다면 변경사항을 Entity가 아닌 View와 통신하는 Dto에서 처리하고
     * 마지막 commit단계만 Entity에서 처리되도록 한다면 클래스에 영향이 덜 미칠것이다.
     *
     * */
    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.username = post.getUser().getUsername();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
