package com.single.project.scraper.web;

import com.single.project.scraper.model.Company;
import com.single.project.scraper.persist.entity.CompanyEntity;
import com.single.project.scraper.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/company")
@AllArgsConstructor
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword) {
        List<String> result = companyService.getCompanyNamesByKeyword(keyword);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companyEntityList = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyEntityList);
    }

    /**
     * 회사 및 배당금 정보 추가
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("입력하신 티커를 확인할 수 없습니다.");
        }

        Company company = this.companyService.save(ticker);
        companyService.addAutoCompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
