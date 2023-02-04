package com.single.project.scraper;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.model.ScrapedResult;
import com.single.project.scraper.scraper.Scraper;
import com.single.project.scraper.scraper.YahooFinanceScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrapperApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ScrapperApplication.class, args);

        Scraper scraper = new YahooFinanceScraper();
//        ScrapedResult result = scraper.scrap(Company.builder().ticker("O").build());
        Company result = scraper.scrapCompanyByTicker("COKE");
        System.out.println(result.getTicker());
        System.out.println(result.getName());
    }
}
