package com.siemens.trail.controller;

import com.siemens.trail.model.FileMetaData;
import com.siemens.trail.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService service;

    @PostMapping("/upload")
    public ResponseEntity<FileMetaData> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {
        try {
            FileMetaData metadata = service.uploadFile(file, userId);
            return ResponseEntity.ok(metadata);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Return 404 if user is not found
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        Path filePath = service.getFilePath(fileName);

        // Check if file exists
        FileSystemResource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Set headers for content type and content disposition
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";  // Default to binary stream
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileMetaData>> getFilesByUser(@PathVariable Long userId) {
        List<FileMetaData> files = service.getFilesByUserId(userId);
            return ResponseEntity.ok(files);
    }
}
