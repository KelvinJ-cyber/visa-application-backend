package com.kelvin.visa_application_site.Users.repo;

import com.kelvin.visa_application_site.Users.model.Documents;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepo extends JpaRepository<Documents, Long> {

    List<Documents> findByVisaApplication(VisaApplications applications);
}
