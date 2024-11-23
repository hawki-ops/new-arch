package com.siemens.trail.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SectionDTO {
    private Long id;
    private String name;
    private List<FieldDTO> fields;
}