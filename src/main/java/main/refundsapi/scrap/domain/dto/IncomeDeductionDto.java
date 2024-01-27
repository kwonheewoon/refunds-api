package main.refundsapi.scrap.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.refundsapi.common.RefundCalcEnum;
import main.refundsapi.common.TaxCalculations;

import java.math.BigDecimal;

import static main.refundsapi.common.TaxCalculations.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record IncomeDeductionDto(
        @JsonProperty("금액") String amount,
        @JsonProperty("소득구분") String incomeType,
        @JsonProperty("총납임금액") String totalPaidAmount){

    public BigDecimal calcAmount(BigDecimal totalAmount){

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal totalPaidAmount = BigDecimal.ZERO;

        // amount에 대한 유효성 검사 및 파싱
        if (this.amount != null && this.amount.contains(",")) {
            amount = new BigDecimal(this.amount.replaceAll(",", ""));
        }

        // totalPaidAmount에 대한 유효성 검사 및 파싱
        if (this.totalPaidAmount != null && this.totalPaidAmount.contains(",")) {
            totalPaidAmount = new BigDecimal(this.totalPaidAmount.replaceAll(",", ""));
        }

        switch (this.incomeType) {
            case "보험료" -> {
                return amount.multiply(CALC_DECIMAL_0_12);
            }
            case "교육비", "기부금" -> {
                return amount.multiply(CALC_DECIMAL_0_15);
            }
            case "의료비" -> {
                return (amount.subtract(totalAmount.multiply(CALC_DECIMAL_0_03))).compareTo(BigDecimal.ZERO) < 0
                        ? BigDecimal.ZERO : (amount.subtract(totalAmount.multiply(CALC_DECIMAL_0_03))).multiply(CALC_DECIMAL_0_15);
            }
        }
        // 퇴직연금
        return totalPaidAmount.multiply(CALC_DECIMAL_0_15);
    }

}
