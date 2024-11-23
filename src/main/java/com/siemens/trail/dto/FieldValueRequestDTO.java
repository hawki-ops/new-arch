package com.siemens.trail.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldValueRequestDTO {

    private Object value;
    private Long userId;
    private Long fieldId;
}