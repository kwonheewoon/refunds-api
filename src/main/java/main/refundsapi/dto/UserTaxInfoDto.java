package main.refundsapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class UserTaxInfoDto {

    private Long id;

    private BigDecimal totalPayment;


    private BigDecimal totalAmountUsed;


    private String incomeCls;
}
