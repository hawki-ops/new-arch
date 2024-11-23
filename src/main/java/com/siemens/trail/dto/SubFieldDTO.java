package com.siemens.trail.dto;

import com.siemens.trail.model.FieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubFieldDTO {
    private Long id;
    private String name;
    private FieldType type;
}
