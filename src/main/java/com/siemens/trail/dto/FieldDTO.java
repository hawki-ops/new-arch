package com.siemens.trail.dto;

import com.siemens.trail.model.FieldType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FieldDTO {
    private Long id;
    private String name;
    private FieldType type;
    private List<FieldOptionDTO> fieldOptions;
    private List<SubFieldDTO> subFields;
}