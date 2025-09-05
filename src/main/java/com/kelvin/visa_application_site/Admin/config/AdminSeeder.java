package com.kelvin.visa_application_site.Admin.config;

import com.kelvin.visa_application_site.Admin.model.Admin;
import com.kelvin.visa_application_site.Admin.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner createAdmin(AdminRepo adminRepo,@Qualifier("adminPasswordEncoder")PasswordEncoder passwordEncoder){
        return args -> {


            if (adminRepo.findByEmail("kelvinosondu419@gmail.com").isEmpty()) {
                Admin admin =  new Admin();

                admin.setFirstName("Kelvin");
                admin.setLastName("Justine");
                admin.getRole();
                admin.setEmail("kelvinosondu419@gmail.com");
                admin.setPassword(passwordEncoder.encode("k@123"));

                adminRepo.save(admin);
                System.out.println("✅ Admin created!");
            } else {
                System.out.println("ℹ️ Admin already exists, skipping seeding.");
            }

        };
    }

}
