package com.sparta.springhw01.entity;

import com.sparta.springhw01.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    /*
     * 게시글 id, 게시글 비밀번호, 제목, 작성자명, 작성 내용, 작성 날짜
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(CommentRequestDto requestDto, Post post, User user){
        this.contents = requestDto.getContents();
        this.post = post;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto){
        this.contents = requestDto.getContents();
    }
}
