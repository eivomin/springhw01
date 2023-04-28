package com.sparta.springhw01.entity;

import com.sparta.springhw01.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 댓글의 좋아요는 0부터 시작
    @ColumnDefault("0")
    @Column(nullable = false)
    private Long likeCount;

    public Comment(CommentRequestDto requestDto, Post post, User user){
        this.contents = requestDto.getContents();
        this.post = post;
        this.user = user;
        this.likeCount = 0L;
    }

    public void update(CommentRequestDto requestDto){
        this.contents = requestDto.getContents();
    }

    public void addLike(){
        this.likeCount += 1;
    }

    public void deleteLike(){
        this.likeCount -= 1;
    }
}
