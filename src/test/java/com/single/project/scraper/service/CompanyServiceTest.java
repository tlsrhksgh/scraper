package com.single.project.scraper.service;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.persist.CompanyRepository;
import com.single.project.scraper.persist.DividendRepository;
import com.single.project.scraper.persist.entity.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private DividendRepository dividendRepository;

    @InjectMocks
    private CompanyService companyService;

    void Company객체_저장() {

    }

    @Test
    void CompanyEntity객체_전체조회() {
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

    }
}