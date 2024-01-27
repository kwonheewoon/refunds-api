package main.refundsapi.scrap.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SalaryInfoDto(
        @JsonProperty("소득내역") String incomeDetail,
        @JsonProperty("총지급액") String totalAmount,
        @JsonProperty("업무시작일") String startDate,
        @JsonProperty("기업명") String companyName,
        @JsonProperty("이름") String name,
        @JsonProperty("지급일") String paymentDate,
        @JsonProperty("업무종료일") String endDate,
        @JsonProperty("주민등록번호") String regNo,
        @JsonProperty("소득구분") String incomeType,
        @JsonProperty("사업자등록번호") String binNo
) {

    public BigDecimal getTotalAmount(){
        return new BigDecimal(totalAmount.replaceAll(",", ""));
    }

}

