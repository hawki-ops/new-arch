package com.siemens.trail.repository;

import com.siemens.trail.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {

    List<FileMetaData> findByUserId(Long userId);
}
