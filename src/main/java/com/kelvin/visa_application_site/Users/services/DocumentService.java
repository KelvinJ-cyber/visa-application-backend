package com.kelvin.visa_application_site.Users.services;

import com.kelvin.visa_application_site.Users.repo.DocumentRepo;
import org.springframework.stereotype.Service;


@Service
public class DocumentService {

    private final DocumentRepo documentRepo;

    public DocumentService(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }


}
