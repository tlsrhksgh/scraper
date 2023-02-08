package com.single.project.scraper.web;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.model.Dividend;
import com.single.project.scraper.model.ScrapedResult;
import com.single.project.scraper.service.FinanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {

    @MockBean
    private FinanceService financeService;

    @Autowired
    private MockMvc mvc;


    @Test
    void 회사이름으로_배당금_조회() throws Exception {
        //given
        Company company = Company.builder()
                .ticker("t")
                .name("test")
                .build();

        List<Dividend> dividendList = Arrays.asList(
                Dividend.builder()
                        .date(LocalDateTime.now())
                        .dividend("0.14")
                        .build(),
                Dividend.builder()
                        .date(LocalDateTime.now())
                        .dividend("0.12")
                        .build(),
                Dividend.builder()
                        .date(LocalDateTime.now())
                        .dividend("0.13")
                        .build()
        );

        given(financeService.getDividendByCompanyName(anyString()))
                .willReturn(new ScrapedResult(company, dividendList));

        //when
        //then
        mvc.perform(get("/finance/dividend/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company.ticker").value("t"))
                .andExpect(jsonPath("$.company.name").value("test"))
                .andExpect(jsonPath("$.dividends[0].dividend").value("0.14"))
                .andExpect(jsonPath("$.dividends[1].dividend").value("0.12"))
                .andExpect(jsonPath("$.dividends[2].dividend").value("0.13"));
    }

}