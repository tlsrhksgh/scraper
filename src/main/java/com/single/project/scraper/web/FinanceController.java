package com.single.project.scraper.web;

import com.single.project.scraper.model.ScrapedResult;
import com.single.project.scraper.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/finance")
@RestController
public class FinanceController {

    private FinanceService financeService;

    @PreAuthorize("isAuthenticated() and hasRole('READ')")
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
        ScrapedResult result = this.financeService.getDividendByCompanyName(companyName);

        return ResponseEntity.ok(result);
    }

}
