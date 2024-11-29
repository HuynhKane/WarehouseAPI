package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Notification;
import com.WarehouseAPI.WarehouseAPI.repository.NotificationRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private  NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }
    @Override
    public ResponseEntity<String> addNotification(Notification notification) {
        notificationRepository.save(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body("Notification added successfully");
    }

    @Override
    public String updateNotification(String _id, Notification notification) {
        Optional<Notification> existingNotificationOpt = notificationRepository.findById(_id);
        if(existingNotificationOpt.isEmpty()){
            return "Notification with ID " + _id + " not found";
        }
        Notification existingNotification = existingNotificationOpt.get();
        existingNotification.setDescription(notification.getDescription());
        existingNotification.setType(notification.getType());
        existingNotification.setTitle(notification.getTitle());
        existingNotification.setTimestamp(notification.getTimestamp());
        notificationRepository.save(existingNotification);

        return "Notification updated successfully";
    }


    @Override
    public String deleteNotification(String _id) {
        Optional<Notification> existingNotificationOpt = notificationRepository.findById(_id);
        if (existingNotificationOpt.isEmpty()) {
            return "Notification not found, deletion failed";
        }
        notificationRepository.deleteById(_id);
        return "Notification deleted successfully";
    }


    @Override
    public Optional<Notification> getNotification(String _id) {
        return notificationRepository.findById(_id);
    }

    @Async
    @Override
    public void sendNotification(Notification notification) {
        try {
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }

    // Method to send a notification to all connected WebSocket clients
    public void sendNotificationToAll(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message payload cannot be null");
        }
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

}
