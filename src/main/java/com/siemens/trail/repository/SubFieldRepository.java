package com.siemens.trail.repository;

import com.siemens.trail.model.SubField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubFieldRepository extends JpaRepository<SubField, Integer> {
    void deleteAllByFieldId(Integer fieldId);
}
