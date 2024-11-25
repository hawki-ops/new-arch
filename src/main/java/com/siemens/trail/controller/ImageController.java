package com.siemens.trail.controller;

import com.siemens.trail.model.Image;
import com.siemens.trail.service.ImageService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    @PostMapping("/upload/{userID}")
    public ResponseEntity<Image> uploadImage(@RequestParam("image") MultipartFile file, @PathVariable Long userID ) {
        try {
            Image savedImage = imageService.saveImage(file, userID);
            return ResponseEntity.ok(savedImage);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            byte[] imageData = imageService.getImage(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
