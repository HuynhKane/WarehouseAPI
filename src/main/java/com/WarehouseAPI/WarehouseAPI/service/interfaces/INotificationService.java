package com.WarehouseAPI.WarehouseAPI.service.interfaces;


import com.WarehouseAPI.WarehouseAPI.model.Notification;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    public ResponseEntity<String> addNotification(Notification notification);
    public String updateNotification(String _id, Notification notification);
    public String deleteNotification(String _id);
    public Optional<Notification> getNotification(String _id);
    public List<Notification> getAllNotification();
    public void sendNotification(Notification notification);
}
