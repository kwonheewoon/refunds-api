package main.refundsapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class UserTaxResultDto {

    private String name;

    private BigDecimal limitAmount;


    private BigDecimal deductible;


    private BigDecimal refundAmount;
}
