package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Users.dto.DocumentResponseDto;
import com.kelvin.visa_application_site.Users.services.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins =  "http://localhost:5173")
@RequestMapping("/api/user/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload/{applicationId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable int applicationId) {
        try {
            documentService.uploadFile(file, applicationId);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long documentId) {
        DocumentResponseDto doc = documentService.downloadFile(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.fileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.fileName() + "\"")
                .body(doc.data());
    }
}