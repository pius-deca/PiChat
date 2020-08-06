package com.github.pius.pichats.dto;

import com.github.pius.pichats.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private String comment;
    private User user;
}
