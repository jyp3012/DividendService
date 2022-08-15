package zerobase.dividendservice.web;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zerobase.dividendservice.model.Company;
import zerobase.dividendservice.model.constants.CacheKey;
import zerobase.dividendservice.persist.entity.CompanyEntity;
import zerobase.dividendservice.service.CompanyService;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final CacheManager redisCacheManager;


    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        //service 인스턴스는 싱긆톤으로 관리가 된다.
        // 인스턴스가 2개 이상일 경우 각 키워드의 공유가 이루어지지 않으므로 조회가 이루어졌을 때 오류가 발생 할 수 있다.
        // Tire
        var result = this.companyService.autocomplete(keyword);
        //Like 연산
//        var result = this.companyService.getCompanyNameByKeyword(keyword);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('READ')")
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * 회사 추가
     */
    @PreAuthorize("hasRole('WRITE')")
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.sava(ticker);
        this.companyService.addAutoCompleteKeyword(company.getName());
        return ResponseEntity.ok(company);
    }

    @PreAuthorize("hasRole('WRITE')")
    @DeleteMapping("/{ticker}")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = companyService.deleteCompany(ticker);
        this.clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName) {
       this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }
}
