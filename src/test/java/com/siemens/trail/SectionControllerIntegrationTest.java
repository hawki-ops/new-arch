package com.siemens.trail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.trail.dto.SectionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateSection() throws Exception {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName("Personal Information");

        String jsonContent = objectMapper.writeValueAsString(sectionDTO);

        mockMvc.perform(post("/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Personal Information"));
    }

    @Test
    void testGetAllSections() throws Exception {
        mockMvc.perform(get("/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
