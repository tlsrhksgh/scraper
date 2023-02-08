package com.single.project.scraper.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScrapedResult {
    private Company company;
    private List<Dividend> dividends;

}
