package com.siemens.trail.service;

import com.siemens.trail.model.FileMetaData;
import com.siemens.trail.model.WebUser;
import com.siemens.trail.repository.FileRepository;
import com.siemens.trail.repository.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.storage.directory}")
    private String uploadDir;

    private final FileRepository fileMetadataRepository;
    private final WebUserRepository webUserRepository; // To fetch WebUser by userId


    public FileMetaData uploadFile(MultipartFile file, Long userId) throws IOException {
        // Fetch the user by userId
        Optional<WebUser> optionalUser = webUserRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        WebUser user = optionalUser.get();

        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // Save file metadata with associated userId in the database
        FileMetaData metadata = new FileMetaData();
        metadata.setFileName(fileName);
        metadata.setFilePath(filePath.toString());
        metadata.setUser(user);

        return fileMetadataRepository.save(metadata);
    }

    public List<FileMetaData> getFilesByUserId(Long userId) {
        return fileMetadataRepository.findByUserId(userId);
    }

    // Method to retrieve file path by filename
    public Path getFilePath(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
}
