package com.kelvin.visa_application_site.Admin.dto;


public record AdminAuthResponse (
         String firstName,
         String lastName,
          String token,
         String role,
         String email

){}
