package main.refundsapi.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Builder
public class UserTaxResultApiDto {

    private String name;

    private BigDecimal limitAmount;


    private BigDecimal deductible;


    private BigDecimal refundAmount;
}
