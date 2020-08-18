package com.github.pius.pichats.dto;

import java.sql.Timestamp;

import com.github.pius.pichats.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private String comment;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private User user;
}
