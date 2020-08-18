package com.github.pius.pichats.controller;

import com.github.pius.pichats.service.implementation.NotificationQueueServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
@CrossOrigin
public class NotificationController {
    private final NotificationQueueServiceImpl notification;

    @Autowired
    public NotificationController(NotificationQueueServiceImpl notification) {
        this.notification = notification;
    }

    @GetMapping()
    public void receive() throws Exception {
        notification.recieveNotification();
    }

}