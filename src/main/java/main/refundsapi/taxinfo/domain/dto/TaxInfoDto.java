package main.refundsapi.taxinfo.domain.dto;

import lombok.Getter;
import main.refundsapi.scrap.domain.dto.JsonListDto;
import main.refundsapi.scrap.domain.dto.ResponseDto;
import main.refundsapi.scrap.domain.dto.ScrapResponseDto;
import main.refundsapi.scrap.enums.ScrapCode;
import main.refundsapi.scrap.exception.ScrapException;

import java.math.BigDecimal;


public record TaxInfoDto(BigDecimal eitca, BigDecimal sptda, BigDecimal cta, BigDecimal stda, BigDecimal rptda) {

//
//    // 근로소득세액공제금액
//    private final BigDecimal eitca;
//    // 특별세액공제금액
//    private final double sptda;
//    // 산출세액
//    private final int cta;
//    // 표준세액공제금액
//    private final int stda;
//    // 퇴직연금세액공제금액
//    private final double rptda;
//
//    public TaxInfoDto(ResponseDto responseDto){
//
//        if(null == responseDto || null == responseDto.jsonListData()){
//            throw new ScrapException(ScrapCode.SCRAP_FAIL);
//        }
//
//        JsonListDto jsonListDto = responseDto.jsonListData();
//
//        this.eitca = jsonListDto.calcEitca();
//        this.sptda = jsonListDto.calcSptda();
//        this.cta = jsonListDto.calcTaxAmount();
//
//        if(this.sptda <= 130_000 ) this.stda = 130_000;
//        else if(this.sptda > 130_000) this.stda = 0;
//        else this.stda = 0;
//
//        this.rptda = jsonListDto.calcRptda();
//    }
}
