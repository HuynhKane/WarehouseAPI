package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Notification;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.INotificationService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final INotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(INotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public List<Notification> getAllNotificationDetails() {
        return notificationService.getAllNotification();
    }

    @GetMapping("/{id}")
    public Optional<Notification> getNotificationDetails(@PathVariable String id) {
        return notificationService.getNotification(id);
    }

    @PostMapping
    public String addNotificationDetails(@RequestBody Notification notification) {
        notificationService.addNotification(notification);
        // Broadcast the notification to all users
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        return "Notification added and broadcast successfully";
    }

    @PutMapping("/{id}")
    public String updateNotificationDetails(@PathVariable String id, @RequestBody Notification updatedNotification) {
        notificationService.updateNotification(id, updatedNotification);
        return "Notification updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteNotificationDetails(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return "Notification deleted successfully";
    }

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public Notification sendNotification(@Payload Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        return notification;
    }

}