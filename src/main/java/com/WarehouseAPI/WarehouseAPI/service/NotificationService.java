package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Notification;
import com.WarehouseAPI.WarehouseAPI.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService{

    @Autowired
    NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }
    @Override
    public String addNotification(Notification notification) {
        notificationRepository.save(notification);
        return "add nofitication, ok";
    }

    @Override
    public String updateNotification(String _id, Notification notification) {
        Optional<Notification> existingNofiticationOpt = notificationRepository.findById(_id);
        if(existingNofiticationOpt.isPresent()){
            Notification existingNotification = existingNofiticationOpt.get();
            existingNotification.setIdNotification(notification.getIdNotification());
            existingNotification.setDescription(notification.getDescription());
            existingNotification.setType(notification.getType());
            existingNotification.setTitle(notification.getTitle());
            existingNotification.setTimestamp(notification.getTimestamp());

            return "Update notification, done";
        }
        return "Update notification, failed";
    }


    @Override
    public String deleteNotification(String _id) {
        notificationRepository.deleteById(_id);
        return "Delete notification, ok";
    }

    @Override
    public Notification getNotification(String _id) {
        if(notificationRepository.findById(_id).isEmpty())
        {
            return null;
        }
        return notificationRepository.findById(_id).get();
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }
}
