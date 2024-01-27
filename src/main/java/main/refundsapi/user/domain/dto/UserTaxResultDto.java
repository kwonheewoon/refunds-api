package main.refundsapi.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class UserTaxResultDto {

    private Long id;

    private int year;

    private BigDecimal limitAmount;


    private BigDecimal deductible;


    private BigDecimal refundAmount;
}
