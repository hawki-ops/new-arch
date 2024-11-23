package com.siemens.trail.service;

import com.siemens.trail.dto.SectionRequestDTO;
import com.siemens.trail.model.Section;
import com.siemens.trail.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Section createSection(SectionRequestDTO requestDTO) {
        Section section = new Section();
        section.setName(requestDTO.getName());
        return sectionRepository.save(section);
    }

    public Section updateSection(Long id, SectionRequestDTO requestDTO) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with ID: " + id));
        section.setName(requestDTO.getName());
        return sectionRepository.save(section);
    }

    public void deleteSection(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new RuntimeException("Section not found with ID: " + id);
        }
        sectionRepository.deleteById(id);
    }
}
