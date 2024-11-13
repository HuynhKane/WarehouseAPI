package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface NotificationRepository  extends MongoRepository<Notification, String> {
}
