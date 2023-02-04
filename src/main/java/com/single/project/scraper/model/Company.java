package com.single.project.scraper.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {

    private String ticker;
    private String name;

    Company(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {
        private String ticker;
        private String name;

        CompanyBuilder() {
        }

        public CompanyBuilder ticker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Company build() {
            return new Company(ticker, name);
        }

    }

}
