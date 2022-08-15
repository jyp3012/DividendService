package zerobase.dividendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import zerobase.dividendservice.model.Company;
import zerobase.dividendservice.model.ScrapedResult;
import zerobase.dividendservice.scraper.YahooFinanceScraper;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendServiceApplication.class, args);


    }

}
