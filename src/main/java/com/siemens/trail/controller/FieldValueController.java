package com.siemens.trail.controller;

import com.siemens.trail.dto.AddValueDTO;
import com.siemens.trail.dto.UserFieldValuesResponseDTO;
import com.siemens.trail.service.FieldValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/field-values")
@RequiredArgsConstructor
public class FieldValueController {
    private final FieldValueService fieldValueService;

    @PostMapping
    public ResponseEntity<String> createFieldValues(@RequestBody AddValueDTO addValueDTO) {
        fieldValueService.createFieldValues(addValueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Field values created successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserFieldValuesResponseDTO> getFieldValuesByUser(@PathVariable Long userId) {
        UserFieldValuesResponseDTO response = fieldValueService.getFieldValuesByUser(userId);
        return ResponseEntity.ok(response);
    }
}
