package com.siemens.trail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter @Getter
public class ValueRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "valueRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<FieldValue> values;


}
