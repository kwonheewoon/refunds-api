package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.TaxInfoEnum;
import main.refundsapi.dto.UserDto;
import main.refundsapi.dto.UserTaxResultApiDto;
import main.refundsapi.dto.UserTaxResultDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import main.refundsapi.entity.UserTaxResultEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.repository.UserTaxResultRepository;
import main.refundsapi.util.SecurityUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserRefundService {

    private final WebClient webClient;

    private final UserRepository userRepository;

    private final UserTaxInfoRepository userTaxInfoRepository;

    private final UserTaxResultRepository userTaxResultRepository;

    public UserTaxResultApiDto refund() throws ParseException {

        LocalDate now = LocalDate.now();

        UserTaxResultDto userTaxResultDto = UserTaxResultDto.builder().build();

        //현재 년도
        int year = now.getYear();

        var findUserEntity = SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                );

        var findUserTaxInfoEntity = userTaxInfoRepository.findByUserEntityAndYear(findUserEntity, year).orElseThrow();

        //총급여액
        BigDecimal totalPayment = findUserTaxInfoEntity.getTotalPayment();
        userTaxResultDto.setRefundAmount(refundAmountCalc(totalPayment));

        //총사용금액
        BigDecimal totalAmountUsed = findUserTaxInfoEntity.getTotalAmountUsed();

        //소득구분
        String incomeCls = findUserTaxInfoEntity.getIncomeCls();

        return UserTaxResultApiDto.builder().build();
    }

    public BigDecimal refundAmountCalc(BigDecimal totalPayment){
        //근로소득 세액공제 한도 계산
        //총급여액 3,300만원 이하
        if(totalPayment.compareTo(new BigDecimal(33000000))  == -1){
            //한도 74만원 세팅
            return new BigDecimal(740000);
        }
        //총급여액 3,300만원 초과 7,000만원 이하
        else if((totalPayment.compareTo(new BigDecimal(33000000))  == 1) &&
                totalPayment.compareTo(new BigDecimal(70000000))  == -1){

            //74만원 - [(총급여액 -3,300만원) x 0.008] 다만, 위금액이 66만원보다
            //적은경우 66만원
            BigDecimal refundAmount = new BigDecimal(740000).subtract(
                    new BigDecimal(33000000).multiply(new BigDecimal("0.008"))
            );

            return refundAmount.compareTo(new BigDecimal(660000)) == -1 ?
                            new BigDecimal(660000) :
                            refundAmount;
        }
        //총급여액 7,000만원 초과
        else if(totalPayment.compareTo(new BigDecimal(70000000))  == 1){
            BigDecimal refundAmount = new BigDecimal(660000).subtract(
                    new BigDecimal(70000000).multiply(new BigDecimal("0.5"))
            );

            return refundAmount.compareTo(new BigDecimal(500000)) == -1 ?
                            new BigDecimal(500000) :
                            refundAmount;
        }
        return new BigDecimal(740000);
    }


}
