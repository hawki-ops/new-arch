package com.siemens.trail.service;

import com.siemens.trail.dto.FieldOptionDTO;
import com.siemens.trail.dto.FieldRequestDTO;
import com.siemens.trail.dto.SubFieldDTO;
import com.siemens.trail.model.*;
import com.siemens.trail.repository.FieldOptionRepository;
import com.siemens.trail.repository.FieldRepository;
import com.siemens.trail.repository.SectionRepository;
import com.siemens.trail.repository.SubFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;
    private final SectionRepository sectionRepository;
    private final SubFieldRepository subFieldRepository;
    private final FieldOptionRepository fieldOptionRepository;

    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    private FieldOption toEntity(FieldOptionDTO fieldOptionDTO) {
        FieldOption fieldOption = new FieldOption();
        fieldOption.setId(fieldOptionDTO.getId());
        fieldOption.setName(fieldOptionDTO.getName());
        return fieldOption;
    }

    private SubField toEntity(SubFieldDTO subFieldDTO) {
        SubField subField = new SubField();
        subField.setId(subFieldDTO.getId());
        subField.setName(subFieldDTO.getName());
        subField.setType(subFieldDTO.getType());
        return subField;
    }

    public Field createField(FieldRequestDTO requestDTO) {
        Section section = sectionRepository.findById(requestDTO.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with ID: " + requestDTO.getSectionId()));

        Field field = new Field();
        field.setName(requestDTO.getName());
        field.setType(requestDTO.getType());
        field.setSection(section);

        if (field.getType() == FieldType.OPTION && requestDTO.getOptions() != null) {
            List<FieldOption> options = requestDTO.getOptions().stream()
                    .map(this::toEntity)
                    .peek(option -> option.setField(field))
                    .toList();
            field.setOptions(options);
        } else if (field.getType() == FieldType.SUB && requestDTO.getSubFields() != null) {
            List<SubField> subFields = requestDTO.getSubFields().stream()
                    .map(this::toEntity)
                    .peek(subField -> subField.setField(field))
                    .toList();
            field.setSubFields(subFields);
        }


        return fieldRepository.save(field);
    }

    public Field updateField(Long id, FieldRequestDTO requestDTO) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + id));

        boolean differentType = !field.getType().equals(requestDTO.getType());

        if (differentType) {
            if (field.getType() == FieldType.OPTION) {
                fieldOptionRepository.deleteAllByFieldId(Math.toIntExact(id));
            } else if (field.getType() == FieldType.SUB) {
                subFieldRepository.deleteAllByFieldId(Math.toIntExact(id));
            }
        }

        field.setName(requestDTO.getName());
        field.setType(requestDTO.getType());

        if (field.getType() == FieldType.OPTION) {
            List<FieldOption> options = requestDTO.getOptions().stream()
                    .map(this::toEntity)
                    .peek(option -> option.setField(field))
                    .toList();
            field.setOptions(options);
        }

        // Update SubFields
        if (field.getType() == FieldType.SUB) {
            List<SubField> subFields = requestDTO.getSubFields().stream()
                    .map(this::toEntity)
                    .peek(subField -> subField.setField(field))
                    .toList();
            field.setSubFields(subFields);
        }

        return fieldRepository.save(field);
    }

    public void deleteField(Long id) {
        if (!fieldRepository.existsById(id)) {
            throw new RuntimeException("Field not found with ID: " + id);
        }
        fieldRepository.deleteById(id);
    }

}
