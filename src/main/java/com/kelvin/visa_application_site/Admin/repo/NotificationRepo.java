package com.kelvin.visa_application_site.Admin.repo;

import com.kelvin.visa_application_site.Admin.dto.NotificationResponse;
import com.kelvin.visa_application_site.Admin.model.Notification;
import com.kelvin.visa_application_site.Users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer> {
    List<NotificationResponse> findByUser(Users user);
}
