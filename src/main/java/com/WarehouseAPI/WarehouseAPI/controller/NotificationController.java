package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Notification;
import com.WarehouseAPI.WarehouseAPI.service.INotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAllNotificationDetails() {
        return notificationService.getAllNotification();
    }

    @GetMapping("/{id}")
    public Notification getNotificationDetails(@PathVariable String id) {
        return notificationService.getNotification(id);
    }

    @PostMapping
    public String addNotificationDetails(@RequestBody Notification notification) {
        notificationService.addNotification(notification);
        return "Notification added successfully";
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
}