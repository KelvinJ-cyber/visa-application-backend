package com.kelvin.visa_application_site.Users.repo;

import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisaApplicationRepo extends JpaRepository<VisaApplications, Integer> {
    List<VisaApplications> findByUser(Users users);
}
