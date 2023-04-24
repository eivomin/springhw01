package com.sparta.springhw01.entity;

import com.sparta.springhw01.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    /*
    * 게시글 id, 게시글 비밀번호, 제목, 작성자명, 작성 내용, 작성 날짜
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}

