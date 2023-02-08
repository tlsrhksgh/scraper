package com.single.project.scraper.scheduler;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.model.ScrapedResult;
import com.single.project.scraper.persist.CompanyRepository;
import com.single.project.scraper.persist.DividendRepository;
import com.single.project.scraper.persist.entity.CompanyEntity;
import com.single.project.scraper.persist.entity.DividendEntity;
import com.single.project.scraper.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

//    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companyEntityList = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity company : companyEntityList) {
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = yahooFinanceScraper.scrap(Company.builder()
                                                                            .name(company.getName())
                                                                            .ticker(company.getTicker())
                                                                            .build());

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    // dividend 모델을 dividend entity로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // element를 하나씩 검증 후 dividend repository에 삽입
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists) {
                            dividendRepository.save(e);
                        }
                    });

            // 연속적인 스크래핑 방지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }
    }
}
