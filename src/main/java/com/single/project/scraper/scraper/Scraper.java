package com.single.project.scraper.scraper;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
