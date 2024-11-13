package com.WarehouseAPI.WarehouseAPI.controller;



import com.WarehouseAPI.WarehouseAPI.model.Notification;
import com.WarehouseAPI.WarehouseAPI.service.INotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

     INotificationService iNotificationService;

    public NotificationController(INotificationService iNotificationService){
        this.iNotificationService = iNotificationService;
    }

    @GetMapping("/all")
    public List<Notification> getAllNotificationDetails(){
        return iNotificationService.getAllNotification();
    }

    @GetMapping("/{id}/get")
    public Notification getNotificationDetails(@PathVariable("_id") String _id){
        return iNotificationService.getNotification(_id);
    }

    @PostMapping("/add")
    public String addNotificationDetails(@RequestBody Notification notification){
        iNotificationService.addNotification(notification);
        return "Notification was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateNotificationDetails(@PathVariable("_id") String _id, @RequestBody Notification updatedNotification){
        iNotificationService.updateNotification(_id, updatedNotification);
        return "Notification was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteNotificationDetails(@PathVariable("_id") String _id){
        iNotificationService.deleteNotification(_id);
        return "Notification was deleted successfully";
    }
}
