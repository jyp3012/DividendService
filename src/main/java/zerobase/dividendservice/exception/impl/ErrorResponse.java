package zerobase.dividendservice.exception.impl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private int code;
    private String massage;
}
