package com.WarehouseAPI.WarehouseAPI.service;


import com.WarehouseAPI.WarehouseAPI.model.Notification;

import java.util.List;

public interface INotificationService {
    public String addNotification(Notification notification);
    public String updateNotification(String _id, Notification notification);
    public String deleteNotification(String _id);
    public Notification getNotification(String _id);
    public List<Notification> getAllNotification();
}
