package com.single.project.scraper.service;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.model.Dividend;
import com.single.project.scraper.model.ScrapedResult;
import com.single.project.scraper.model.constants.CacheKey;
import com.single.project.scraper.persist.CompanyRepository;
import com.single.project.scraper.persist.DividendRepository;
import com.single.project.scraper.persist.entity.CompanyEntity;
import com.single.project.scraper.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;


    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다"));

        // 2. 조회된 회사 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntityList = dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntityList.stream()
                .map(e -> Dividend.builder()
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build())
                .collect(Collectors.toList());

        return new ScrapedResult(Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(),
                dividends);
    }

}
