package com.siemens.trail.dto;

import com.siemens.trail.model.FieldType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FieldRequestDTO {
    private String name;
    private FieldType type;
    private List<FieldOptionDTO> options;
    private List<SubFieldDTO> subFields;
    private Long sectionId; // For associating the field with a section
}
