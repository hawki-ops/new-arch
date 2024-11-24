package com.siemens.trail.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@Entity
public class FieldValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long numberValue;

    String stringValue;

    @Temporal(TemporalType.DATE)
    Calendar dateValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    WebUser user;

    @ManyToOne
    @JoinColumn(name = "field_id")
    Field field;

    @ManyToOne
    @JoinColumn(name = "sub_id")
    SubField subField;
}