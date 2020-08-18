package com.github.pius.pichats.dto;

import java.util.List;

import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchedUsersReponseDTO extends User {

    private List<ProfilePic> profile;

}