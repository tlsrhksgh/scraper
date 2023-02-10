package com.single.project.scraper.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.project.scraper.model.Company;
import com.single.project.scraper.persist.entity.CompanyEntity;
import com.single.project.scraper.service.CompanyService;
import org.apache.commons.collections4.Trie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;


@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @MockBean
    private CompanyService companyService;

    @MockBean
    private Trie trie;

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

    @Test
    void 문자열_자동완성_조회() throws Exception {
        //given
        trie.put("test", null);
        trie.put("test2", null);
        trie.put("test3", null);
        trie.put("test4", null);
        given(companyService.autoComplete(anyString()))
                .willReturn((List<String>) trie.prefixMap(anyString()).keySet()
                        .stream().collect(Collectors.toList()));

        //when
        //then
        mvc.perform(get("/company/autocomplete").param("t")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value("test"))
                .andExpect(jsonPath("$.[0]").value("test1"))
                .andExpect(jsonPath("$.[0]").value("test2"))
                .andExpect(jsonPath("$.[0]").value("test3"));
    }

}