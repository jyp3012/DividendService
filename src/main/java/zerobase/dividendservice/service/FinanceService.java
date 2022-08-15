package zerobase.dividendservice.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.dividendservice.exception.impl.NoCompanyException;
import zerobase.dividendservice.model.Company;
import zerobase.dividendservice.model.Dividend;
import zerobase.dividendservice.model.ScrapedResult;
import zerobase.dividendservice.model.constants.CacheKey;
import zerobase.dividendservice.persist.CompanyRepository;
import zerobase.dividendservice.persist.DividendRepository;
import zerobase.dividendservice.persist.entity.CompanyEntity;
import zerobase.dividendservice.persist.entity.DividendEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 요청이 자주 들어오는가?
    // 자주 변경되는 데이터 인가?
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사정보를 조회
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        // 2. 조회된 회사의 id로 배당금을 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(new Company(companyEntity.getTicker(), companyEntity.getName()),dividends);


    }
}
