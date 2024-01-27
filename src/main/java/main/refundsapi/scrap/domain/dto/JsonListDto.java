package main.refundsapi.scrap.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.refundsapi.common.RefundCalcEnum;
import main.refundsapi.common.TaxCalculations;
import main.refundsapi.scrap.enums.ScrapCode;
import main.refundsapi.scrap.exception.ScrapException;

import java.math.BigDecimal;
import java.util.List;

import static main.refundsapi.common.TaxCalculations.CALC_DECIMAL_0_55;

public record JsonListDto(
        @JsonProperty("급여") List<SalaryInfoDto> salary,
        @JsonProperty("산출세액") String taxAmount,
        @JsonProperty("소득공제") List<IncomeDeductionDto> incomeDeductions
) {

    public BigDecimal calcTaxAmount(){
        if(null == taxAmount || taxAmount.isBlank()) throw new ScrapException(ScrapCode.SCRAP_FAIL);
        return new BigDecimal(taxAmount.replaceAll(",", ""));
    }

    public BigDecimal calcSptda(){

        BigDecimal totalAmount = salary.stream().findFirst().orElseThrow(() -> new ScrapException(ScrapCode.SCRAP_FAIL)).getTotalAmount();

        return incomeDeductions.stream().filter(incomeDeductionDto -> !"퇴직연금".equals(incomeDeductionDto.incomeType()))
                .map(incomeDeductionDto -> incomeDeductionDto.calcAmount(totalAmount))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcRptda(){

        BigDecimal totalAmount = salary.stream().findFirst().orElseThrow(() -> new ScrapException(ScrapCode.SCRAP_FAIL)).getTotalAmount();

        return incomeDeductions.stream().filter(incomeDeductionDto -> "퇴직연금".equals(incomeDeductionDto.incomeType()))
                .map(incomeDeductionDto -> incomeDeductionDto.calcAmount(totalAmount))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 근로소득세액공제금액 계산
    public BigDecimal calcEitca(){
        if(null == taxAmount || taxAmount.isBlank()) throw new ScrapException(ScrapCode.SCRAP_FAIL);

        return new BigDecimal(taxAmount.replaceAll(",", "")).multiply(CALC_DECIMAL_0_55);
    }
}
