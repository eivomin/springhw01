package com.sparta.springhw01.entity;

import com.sparta.springhw01.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Post extends Timestamped {
    /*
    * 게시글 id, 게시글 비밀번호, 제목, 작성자명, 작성 내용, 작성 날짜
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 좋아요는 0부터 시작
    @ColumnDefault("0")
    @Column(nullable = false)
    private Long likeCount;

    public Post(PostRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
        this.likeCount = 0L;
    }

    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void addLike(){
        this.likeCount += 1;
    }

    public void deleteLike(){
        this.likeCount -= 1;
    }

}

