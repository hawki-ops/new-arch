package com.siemens.trail.service;

import com.siemens.trail.dto.AddValueDTO;
import com.siemens.trail.dto.UserFieldValuesResponseDTO;
import com.siemens.trail.model.*;
import com.siemens.trail.repository.FieldOptionRepository;
import com.siemens.trail.repository.FieldRepository;
import com.siemens.trail.repository.FieldValueRepository;
import com.siemens.trail.repository.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FieldValueService {
    private final FieldValueRepository fieldValueRepository;
    private final FieldRepository fieldRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final WebUserRepository webUserRepository;
    @Value("${image.storage.directory}")
    private String storageDirectory;

    public void createFieldValues(AddValueDTO addValueDTO) {
        // Fetch user
        WebUser user = webUserRepository.findById(addValueDTO.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Process each value
        List<FieldValue> fieldValues = addValueDTO.getValues().stream().map(valueDTO -> {
            // Fetch field
            Field field = fieldRepository.findById(valueDTO.getFieldID())
                    .orElseThrow(() -> new RuntimeException("Field not found"));

            // Create FieldValue based on type
            FieldValue fieldValue = new FieldValue();
            fieldValue.setUser(user);
            fieldValue.setField(field);

            // Set value based on FieldType
            switch (valueDTO.getFieldType()) {
                case FILE:
                    try {
                        fieldValue.setStringValue(saveImage( (MultipartFile) valueDTO.getValue() ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case STRING:
                    fieldValue.setStringValue((String) valueDTO.getValue());
                    break;
                case OPTION:
                case SUB:
                case NUMBER:
                    fieldValue.setNumberValue(((Number) valueDTO.getValue()).longValue());
                    break;
                case DATE:
                    try {
                        fieldValue.setDateValue(retrieveDate((String) valueDTO.getValue()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                default:
//                    throw new RuntimeException("Unsupported FieldType: " + valueDTO.getFieldType());
            }

            return fieldValue;
        }).toList();

        // Save all FieldValues
        fieldValueRepository.saveAll(fieldValues);
    }

    public UserFieldValuesResponseDTO getFieldValuesByUser(Long userId) {
        // Fetch the user
        WebUser user = webUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all field values for the user
        List<FieldValue> fieldValues = fieldValueRepository.findAllByUserId(user.getId());

        Map<String, UserFieldValuesResponseDTO.SectionDTO> sections = new HashMap<>();

        for (FieldValue fieldValue : fieldValues) {
            Field field = fieldValue.getField();
            Section section = field.getSection();

            UserFieldValuesResponseDTO.SectionDTO sectionDTO = sections.computeIfAbsent(section.getName(), key -> {
                UserFieldValuesResponseDTO.SectionDTO dto = new UserFieldValuesResponseDTO.SectionDTO();
                dto.setName(section.getName());
                dto.setFields(new ArrayList<>());
                return dto;
            });

            UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO fieldValueDTO = new UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO();

            fieldValueDTO.setName(field.getName());

            switch (field.getType()) {
                case FILE:
                    try {
                        fieldValueDTO.setValue( getImage(fieldValue.getStringValue() ) );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case STRING:
                    fieldValueDTO.setValue(fieldValue.getStringValue());
                    break;
                case OPTION:
                    FieldOption fieldOption = fieldOptionRepository.findById(Math.toIntExact(fieldValue.getNumberValue()))
                            .orElseThrow(() -> new RuntimeException("Option not found"));
                    fieldValueDTO.setValue(fieldOption.getName());
                    break;
                case SUB:
                    break;
                case NUMBER:
                    fieldValueDTO.setValue(fieldValue.getNumberValue());
                    break;
                case DATE:
                    fieldValueDTO.setValue(fieldValue.getDateValue());
                    break;
            }

            sectionDTO.getFields().add(fieldValueDTO);

        }

        // Map sections to the response DTO
        List<UserFieldValuesResponseDTO.SectionDTO> sectionDTOs = new ArrayList<>(sections.values());

        UserFieldValuesResponseDTO responseDTO = new UserFieldValuesResponseDTO();
        responseDTO.setSections(sectionDTOs);

        return responseDTO;
    }

    private Calendar retrieveDate(String dateString ) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Expected yyyy-MM-dd.", e);
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        // Ensure storage directory exists
        File storageDir = new File(storageDirectory);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // Save file to storage directory
        String filePath = Paths.get(storageDirectory, file.getOriginalFilename()).toString();
        file.transferTo(new File(filePath));

        return filePath;
    }

    private byte[] getImage(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

}
