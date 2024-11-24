package com.siemens.trail.dto;

import com.siemens.trail.model.FieldType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class UserFieldValuesResponseDTO {
    private List<SectionDTO> sections;

    @Setter @Getter
    public static class SectionDTO {
        private String name;
        private List<FieldValueDTO> fields;

        @Setter @Getter
        public static class FieldValueDTO {
            private String name;
            private FieldType type;
            private Object value;

            private List<SubFieldValueDTO> values;

            @Setter @Getter
            public static class SubFieldValueDTO {
                private String name;
                private Object value;
            }

        }
    }
}
