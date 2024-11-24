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
        private List<CompositeDTO> values;

        @Getter @Setter
        public static class CompositeDTO {
            private Integer compositeID;
            private FieldType fieldType;
            private Object value;
        }

    }
}
