package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.RefundCalcEnum;
import main.refundsapi.dto.UserTaxResultApiDto;
import main.refundsapi.dto.UserTaxResultDto;
import main.refundsapi.entity.UserTaxResultEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.repository.UserTaxResultQueryRepository;
import main.refundsapi.repository.UserTaxResultRepository;
import main.refundsapi.util.CommonUtil;
import main.refundsapi.util.SecurityUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserRefundService {

    private final WebClient webClient;

    private final UserRepository userRepository;

    private final UserTaxInfoRepository userTaxInfoRepository;

    private final UserTaxResultRepository userTaxResultRepository;

    private final UserTaxResultQueryRepository userTaxResultQueryRepository;

    @Transactional
    public JSONObject refund() throws ParseException {

        UserTaxResultDto userTaxResultDto = UserTaxResultDto.builder().year(CommonUtil.getYear()).build();

        var findUserEntity = SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                );

        var findUserTaxInfoEntity = userTaxInfoRepository.findByUserEntityAndYear(findUserEntity, CommonUtil.getYear()).orElseThrow(() -> new CommonException(ErrorCode.USER_TAX_INFO_NOT_FOUND));

        //총급여액
        BigDecimal totalPayment = findUserTaxInfoEntity.getTotalPayment();
        userTaxResultDto.setLimitAmount(limitAmountCalc(totalPayment).orElseThrow(() -> new CommonException(ErrorCode.LIMIT_AMOUNT_CALC_FAIL)));

        //총사용금액
        BigDecimal totalAmountUsed = findUserTaxInfoEntity.getTotalAmountUsed();
        userTaxResultDto.setDeductible(deductibleCalc(totalAmountUsed, findUserTaxInfoEntity.getIncomeCls()).orElseThrow(() -> new CommonException(ErrorCode.DEDUCTIBLE_CALC_FAIL)));

        //환급액 계산
        userTaxResultDto.setRefundAmount(refundAmountCalc(userTaxResultDto.getDeductible(), userTaxResultDto.getLimitAmount()).orElseThrow(() -> new CommonException(ErrorCode.REFUND_AMOUNT_CALC_FAIL)));

        //환급액 저장 수정 여부 확인을 위하여 조회
        var findUserTaxResultDto = userTaxResultQueryRepository.findRefund(findUserEntity.getUserId(), CommonUtil.getYear());

        if(findUserTaxResultDto.isPresent()){
            //환급액 수정
            userTaxResultRepository.save(
                    UserTaxResultEntity.builder()
                            .id(findUserTaxResultDto.get().getId())
                            .year(userTaxResultDto.getYear())
                            .limitAmount(userTaxResultDto.getLimitAmount())
                            .deductible(userTaxResultDto.getDeductible())
                            .refundAmount(userTaxResultDto.getRefundAmount())
                            .userEntity(findUserEntity)
                            .userTaxInfoEntity(findUserTaxInfoEntity)
                            .build()
            );
        }else{
            //환급액 저장
            userTaxResultRepository.save(
                    UserTaxResultEntity.builder()
                            .year(userTaxResultDto.getYear())
                            .limitAmount(userTaxResultDto.getLimitAmount())
                            .deductible(userTaxResultDto.getDeductible())
                            .refundAmount(userTaxResultDto.getRefundAmount())
                            .userEntity(findUserEntity)
                            .userTaxInfoEntity(findUserTaxInfoEntity)
                            .build()
            );
        }

        //환급액 조회
        var findRefundEntity = userTaxResultQueryRepository.findRefund(findUserEntity.getUserId(), CommonUtil.getYear()).orElseThrow(() -> new CommonException(ErrorCode.USER_TAX_RESULT_NOT_FOUND));
        
        //조회된 환급액 정보 기준으로 json 객체 데이터 반환
        return setRefundJson(findRefundEntity);
    }

    public Optional<BigDecimal> limitAmountCalc(BigDecimal totalPayment){

        //근로소득 세액공제 한도 계산
        //총급여액 3,300만원 이하
        if(totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_3300.getAmount())  == -1){
            //한도 74만원 세팅
            return Optional.of(RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_74.getAmount());
        }
        //총급여액 3,300만원 초과 7,000만원 이하
        else if((totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_3300.getAmount())  == 1) &&
                totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())  == -1){

            //74만원 - [(총급여액 -3,300만원) x 0.008] 다만, 위금액이 66만원보다
            //적은경우 66만원
            BigDecimal refundAmount = RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_74.getAmount().subtract(
                    totalPayment.subtract(RefundCalcEnum.TOTAL_PAYMENT_3300.getAmount())
                            .multiply(new BigDecimal("0.008"))
            );

            return Optional.of(refundAmount.compareTo(new BigDecimal(660000)) == -1 ?
                    RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_66.getAmount() :
                    refundAmount);
        }
        //총급여액 7,000만원 초과
        else if(totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())  == 1){
            BigDecimal refundAmount = RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_66.getAmount().subtract(
                    totalPayment.subtract(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())
                            .multiply(new BigDecimal("0.5"))
            );

            return Optional.of(refundAmount.compareTo(RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_50.getAmount()) == -1 ?
                    RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_50.getAmount() :
                    refundAmount);
        }

        return Optional.empty();
    }

    public Optional<BigDecimal> deductibleCalc(BigDecimal totalAmountUsed, String incomeCls){

        //산출세액이 1,300,000만원 보다 작을경우
        if (totalAmountUsed.compareTo(RefundCalcEnum.CALCULATED_TAX_130.getAmount()) == -1) {
            return Optional.of(totalAmountUsed.multiply(new BigDecimal("0.55")));
        }

        //산출세액이 1,300,000만원 초과일 경우
        else if (totalAmountUsed.compareTo(RefundCalcEnum.CALCULATED_TAX_130.getAmount()) == 1) {
            return Optional.of(RefundCalcEnum.CALCULATED_TAX_71_5.getAmount().add(totalAmountUsed.subtract(
                    RefundCalcEnum.CALCULATED_TAX_130.getAmount()
            ).multiply(new BigDecimal("0.3"))));
        }
        return Optional.empty();
    }

    public Optional<BigDecimal> refundAmountCalc(BigDecimal deductible, BigDecimal limitAmount){

        if(null != deductible && null != limitAmount){
            return Optional.of(deductible.min(limitAmount));
        }

        return Optional.empty();
    }

    public JSONObject setRefundJson(UserTaxResultApiDto userTaxResultApiDto){
        JSONObject result = new JSONObject();

        result.put("이름", userTaxResultApiDto.getName());
        result.put("한도", userTaxResultApiDto.getLimitAmount());
        result.put("공제액", userTaxResultApiDto.getDeductible());
        result.put("환급액", userTaxResultApiDto.getRefundAmount());

        return result;
    }


}
