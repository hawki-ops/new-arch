package com.siemens.trail.repository;

import com.siemens.trail.model.FieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FieldValueRepository extends JpaRepository<FieldValue, Long> {

    List<FieldValue> findAllByUserId(Long userId);

    List<FieldValue> findAllByFieldId(Integer fieldId);
}
