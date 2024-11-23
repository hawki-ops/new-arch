package com.siemens.trail.repository;

import com.siemens.trail.model.FieldOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldOptionRepository extends JpaRepository<FieldOption, Integer> {

    void deleteAllByFieldId(Integer fieldId);
}
