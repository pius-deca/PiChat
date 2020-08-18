package com.github.pius.pichats.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "notifications")
public class Notification extends GeneralBaseEntity {
    private String message;

    private String actor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User subject;
}