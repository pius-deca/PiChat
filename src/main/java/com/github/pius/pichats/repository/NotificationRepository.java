package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}