package com.siemens.trail.dto;

import com.siemens.trail.model.FieldType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class AddValueDTO {
    private Long userID;
    private List<ValueDTO> values;

    @Setter @Getter
    public static class ValueDTO {
        private Long fieldID;
        private FieldType fieldType;
        private Object value;
    }
}
