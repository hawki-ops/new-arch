package com.siemens.trail.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class FieldValueDTO {
    private Long id;
    private Long numberValue;
    private String stringValue;
    private Calendar dateValue;
    private Long userId;
    private Long fieldId;
}