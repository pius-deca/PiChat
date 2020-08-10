package com.github.pius.pichats.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String caption;
    private String post;
    private String url;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @JsonIgnore
    private List<Like> likes;
    @JsonIgnore
    private List<Comment> comments;
    private int numOfLikes;
    private int numOfComments;
    private User user;

}
