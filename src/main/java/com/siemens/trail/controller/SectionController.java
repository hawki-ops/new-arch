package com.siemens.trail.controller;

import com.siemens.trail.dto.SectionRequestDTO;
import com.siemens.trail.model.Section;
import com.siemens.trail.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    @GetMapping
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }

    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody SectionRequestDTO requestDTO) {
        return ResponseEntity.ok(sectionService.createSection(requestDTO));
    }

    @PutMapping("/{id}")
    public Section updateSection(@PathVariable Long id, @RequestBody SectionRequestDTO requestDTO) {
        return sectionService.updateSection(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }
}
