package com.siemens.trail.repository;

import com.siemens.trail.model.ValueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<ValueRecord, Long> {
}
