package com.tdd.domain.comment;

import com.tdd.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {
    public Comment(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
