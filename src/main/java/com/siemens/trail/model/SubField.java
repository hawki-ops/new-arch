package com.siemens.trail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class SubField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    FieldType type;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "subField", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<FieldValue> values;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    @JsonIgnore
    Field field;
}