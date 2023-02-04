package com.single.project.scraper.service;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.model.Dividend;
import com.single.project.scraper.model.ScrapedResult;
import com.single.project.scraper.persist.CompanyRepository;
import com.single.project.scraper.persist.DividendRepository;
import com.single.project.scraper.persist.entity.CompanyEntity;
import com.single.project.scraper.persist.entity.DividendEntity;
import com.single.project.scraper.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CompanyService {

    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if(exists) {
            throw new RuntimeException("이미 있는 ticker 입니다." + ticker);
        }

        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker) {
        // ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("회사 정보가 존재하지 않습니다. ticker: " + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        // 스크래핑 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividends()
                                                .stream().map(e -> new DividendEntity(companyEntity.getId(), e))
                                                .collect(Collectors.toList());
        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }

}