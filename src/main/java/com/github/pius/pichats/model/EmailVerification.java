package com.github.pius.pichats.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

// keep this for now even if its not being used
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity(name = "email_verification")
public class EmailVerification extends GeneralBaseEntity {

    @Column(nullable = false)
    private String code;

    @Future
    private LocalDateTime validity;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}