package com.siemens.trail.service;

import com.siemens.trail.dto.AddValueDTO;
import com.siemens.trail.dto.UserFieldValuesResponseDTO;
import com.siemens.trail.model.*;
import com.siemens.trail.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    private final SubFieldRepository subFieldRepository;
    private final SectionRepository sectionRepository;
    @Value("${image.storage.directory}")
    private String storageDirectory;

    public void createFieldValues(AddValueDTO addValueDTO) {
        // Fetch user
        WebUser user = webUserRepository.findById(addValueDTO.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FieldValue> fieldValues = new ArrayList<>();

        // Process each value

        for (var valueDTO : addValueDTO.getValues()) {
            // Fetch field
            Field field = fieldRepository.findById(valueDTO.getFieldID())
                    .orElseThrow(() -> new RuntimeException("Field not found"));

            if (valueDTO.getFieldType() == FieldType.SUB) {
                for (var subValue : valueDTO.getValues()) {
                    SubField subField = subFieldRepository.findById(subValue.getCompositeID())
                            .orElseThrow(() -> new RuntimeException("SubField not found"));

                    FieldValue fieldValue = new FieldValue();
                    fieldValue.setUser(user);
                    fieldValue.setField(field);
                    fieldValue.setSubField(subField);

                    setValue(fieldValue, subField.getType(), subValue.getValue());

                    fieldValues.add(fieldValue);

                }


            } else {
                FieldValue fieldValue = new FieldValue();
                fieldValue.setUser(user);
                fieldValue.setField(field);

                setValue(fieldValue, valueDTO.getFieldType(), valueDTO.getValue());

                fieldValues.add(fieldValue);
            }

        }
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
        Map<Integer, UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO > subFieldValues = new HashMap<>();

        for (FieldValue fieldValue : fieldValues) {
            Field field = fieldValue.getField();
            Section section = field.getSection();

            UserFieldValuesResponseDTO.SectionDTO sectionDTO = sections.computeIfAbsent(section.getName(), key -> {
                UserFieldValuesResponseDTO.SectionDTO dto = new UserFieldValuesResponseDTO.SectionDTO();
                dto.setName(section.getName());
                dto.setFields(new ArrayList<>());
                return dto;
            });

            if ( field.getType() == FieldType.SUB ) {

                UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO fieldValueDTO = subFieldValues.computeIfAbsent(Math.toIntExact(field.getId()), key -> {
                    UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO dto = new UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO();
                    dto.setName(field.getName());
                    dto.setValues(new ArrayList<>());
                    return dto;
                }  );

                SubField subField = fieldValue.getSubField();

                UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO.SubFieldValueDTO subFieldValueDTO = new UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO.SubFieldValueDTO();

                subFieldValueDTO.setName(subField.getName());

                switch (subField.getType()) {
                    case FILE:
                        try {
                            subFieldValueDTO.setValue(getImage(fieldValue.getStringValue()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case STRING:
                        subFieldValueDTO.setValue(fieldValue.getStringValue());
                        break;
                    case OPTION:
                        FieldOption fieldOption = fieldOptionRepository.findById(Math.toIntExact(fieldValue.getNumberValue()))
                                .orElseThrow(() -> new RuntimeException("Option not found"));
                        subFieldValueDTO.setValue(fieldOption.getName());
                        break;
                    case NUMBER:
                        subFieldValueDTO.setValue(fieldValue.getNumberValue());
                        break;
                    case DATE:
                        subFieldValueDTO.setValue(fieldValue.getDateValue());
                        break;
                }

                fieldValueDTO.setType(field.getType());

                fieldValueDTO.getValues().add(subFieldValueDTO);

//                List<FieldValue> subFieldValues = fieldValueRepository.findAllBy;


            }else {
                UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO fieldValueDTO = new UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO();

                fieldValueDTO.setName(field.getName());

                switch (field.getType()) {
                    case FILE:
                        try {
                            fieldValueDTO.setValue(getImage(fieldValue.getStringValue()));
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
                    case NUMBER:
                        fieldValueDTO.setValue(fieldValue.getNumberValue());
                        break;
                    case DATE:
                        fieldValueDTO.setValue(fieldValue.getDateValue());
                        break;
                }
                fieldValueDTO.setType(field.getType());
                sectionDTO.getFields().add(fieldValueDTO);
            }

        }

        // Map sections to the response DTO


        for ( Map.Entry<Integer, UserFieldValuesResponseDTO.SectionDTO.FieldValueDTO> entry : subFieldValues.entrySet() ) {
            Field field = fieldRepository.findById(Long.valueOf(entry.getKey()))
                            .orElseThrow( () -> new RuntimeException("Field not Found") ) ;
            sections.get(field.getSection().getName()).getFields().add(entry.getValue());

        }

        List<UserFieldValuesResponseDTO.SectionDTO> sectionDTOs = new ArrayList<>(sections.values());

        UserFieldValuesResponseDTO responseDTO = new UserFieldValuesResponseDTO();
        responseDTO.setSections(sectionDTOs);

        return responseDTO;
    }

    private void setValue(FieldValue fieldValue, FieldType fieldType, Object value) {
        // Set value based on FieldType
        switch (fieldType) {
            case FILE:
                try {
                    fieldValue.setStringValue(saveImage((MultipartFile) value));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case STRING:
                fieldValue.setStringValue((String) value);
                break;
            case OPTION:
            case NUMBER:
                fieldValue.setNumberValue(((Number) value).longValue());
                break;
            case DATE:
                try {
                    fieldValue.setDateValue(retrieveDate((String) value));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
//                    throw new RuntimeException("Unsupported FieldType: " + valueDTO.getFieldType());
        }
    }

    private Calendar retrieveDate(String dateString) throws ParseException {
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
