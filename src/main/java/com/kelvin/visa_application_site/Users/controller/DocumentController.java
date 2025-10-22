package com.kelvin.visa_application_site.Users.controller;

import com.kelvin.visa_application_site.Users.dto.DocumentResponseDto;
import com.kelvin.visa_application_site.Users.services.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins =  "https://visa-application-frontend.vercel.app")
@RequestMapping("/api/user/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload/{applicationId}/{documentId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable int applicationId,
                                             @PathVariable Long documentId) {
        try {
            documentService.uploadFile(file, applicationId, documentId);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

//    @GetMapping("/{applicationId}")
//    public ResponseEntity<List<Documents>> getUploadedDocuments(@PathVariable Long applicationId) {
//        List<Documents> documents = documentService.getUploadedDocument(applicationId);
//        return ResponseEntity.ok(documents);
//    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long documentId) {
        DocumentResponseDto doc = documentService.downloadFile(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.fileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.fileName() + "\"")
                .body(doc.data());
    }
}