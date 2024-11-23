package com.siemens.trail.controller;

import com.siemens.trail.dto.FieldRequestDTO;
import com.siemens.trail.model.Field;
import com.siemens.trail.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fields")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @GetMapping
    public List<Field> getAllFields() {
        return fieldService.getAllFields();
    }

    @PostMapping
    public Field createField(@RequestBody FieldRequestDTO requestDTO) {
        return fieldService.createField(requestDTO);
    }

    @PutMapping("/{id}")
    public Field updateField(@PathVariable Long id, @RequestBody FieldRequestDTO requestDTO) {
        return fieldService.updateField(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
    }
}
