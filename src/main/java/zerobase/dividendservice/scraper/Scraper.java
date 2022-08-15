package zerobase.dividendservice.scraper;

import zerobase.dividendservice.model.Company;
import zerobase.dividendservice.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
