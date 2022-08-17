package main.refundsapi.dto;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTaxResultApiDto {

    private Long id;

    private String name;

    private BigDecimal limitAmount;


    private BigDecimal deductible;


    private BigDecimal refundAmount;
}
