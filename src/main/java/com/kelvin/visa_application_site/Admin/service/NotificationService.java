package com.kelvin.visa_application_site.Admin.service;

import com.kelvin.visa_application_site.Admin.dto.NotificationResponse;
import com.kelvin.visa_application_site.Admin.model.Notification;
import com.kelvin.visa_application_site.Admin.repo.NotificationRepo;
import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.repo.UserRepo;
import com.kelvin.visa_application_site.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;

    public NotificationService(NotificationRepo notificationRepo, UserRepo userRepo){
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
    }

    public NotificationResponse createNotification(int userId, Notification data){

          Notification notification = new Notification();
          Users user =  userRepo.findById(userId)
                  .orElseThrow(()-> new UserNotFoundException("User not found"));
          notification.setUser(user);
          notification.setMessage(data.getMessage());
          notification.setTime(LocalDateTime.now());
          notification.setType(data.getType());
          notification.setRead(false);

          Notification savedNotification = notificationRepo.save(notification);

          return new NotificationResponse(
                  savedNotification.getId(),
                  savedNotification.getMessage(),
                  savedNotification.getType(),
                  savedNotification.getTime(),
                  savedNotification.isRead()
          );

    }

    public List<NotificationResponse> getNotification(Users user){
        return notificationRepo.findByUser(user).stream()
                .map(userN -> new NotificationResponse(
                        userN.id(),
                        userN.message(),
                        userN.type(),
                        userN.time(),
                        userN.read()
                )).collect(Collectors.toList());
    }
}


