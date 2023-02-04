package com.single.project.scraper.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.project.scraper.model.Company;
import com.single.project.scraper.persist.entity.CompanyEntity;
import com.single.project.scraper.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;;


@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @MockBean
    private CompanyService companyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void company_추가() throws Exception {
        //given
        given(companyService.save(anyString()))
                .willReturn(Company.builder()
                        .ticker("t")
                        .name("test")
                        .build());

        //when
        //then
        mvc.perform(post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new Company("t", "test")
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("t"))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void company전체가_리턴() throws Exception {
        //given
        Company company = Company.builder()
                .ticker("t")
                .name("test")
                .build();
        Company company2 = Company.builder()
                .ticker("t2")
                .name("test2")
                .build();
        CompanyEntity companyEntity = new CompanyEntity(company);
        CompanyEntity companyEntity2 = new CompanyEntity(company2);

        List<CompanyEntity> companyEntityList = new ArrayList<>();
        companyEntityList.add(companyEntity);
        companyEntityList.add(companyEntity2);

        Page<CompanyEntity> companyEntityPages = new PageImpl<>(companyEntityList);

        given(companyService.getAllCompany(any()))
                .willReturn(companyEntityPages);

        //when

        //then
        mvc.perform(get("/company"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].ticker").value("t"))
                .andExpect(jsonPath("$.content[0].name").value("test"))
                .andExpect(jsonPath("$.content[1].ticker").value("t2"))
                .andExpect(jsonPath("$.content[1].name").value("test2"));
    }

}