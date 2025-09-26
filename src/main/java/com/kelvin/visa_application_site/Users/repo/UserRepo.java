package com.kelvin.visa_application_site.Users.repo;

import com.kelvin.visa_application_site.Users.model.Users;
import com.kelvin.visa_application_site.Users.model.VisaApplications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);
}
