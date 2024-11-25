package com.siemens.trail.service;

import com.siemens.trail.model.Image;
import com.siemens.trail.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${image.storage.directory}")
    private String uploadDir;

    public Image saveImage(MultipartFile file, Long userID ) throws IOException {
        // Save file to the file system
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir+"/"+userID.toString(), fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        // Save path in the database
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFilePath(filePath.toString());
        return imageRepository.save(image);
    }

    public byte[] getImage(Long id) throws IOException {
        // Retrieve file path from the database
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image not found with id: " + id));

        Path filePath = Paths.get(image.getFilePath());
        return Files.readAllBytes(filePath);
    }
}
