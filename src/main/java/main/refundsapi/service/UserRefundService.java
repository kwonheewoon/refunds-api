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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserRefundService {

    private final UserRepository userRepository;

    private final UserTaxInfoRepository userTaxInfoRepository;

    private final UserTaxResultRepository userTaxResultRepository;

    private final UserTaxResultQueryRepository userTaxResultQueryRepository;

    /**
     * 유저 세무정보에 대한 환급액 계산결과 저장
     * */
    @Transactional
    public JSONObject refund() throws ParseException {

        //유저 환급액 계산 결과에 대해 저장, 수정을 위한 dto 생성
        UserTaxResultDto userTaxResultDto = UserTaxResultDto.builder().year(CommonUtil.getYear()).build();

        //유저 조회
        var findUserEntity = SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                );

        //유저 세무정보 조회
        var findUserTaxInfoEntity = userTaxInfoRepository.findByUserEntityAndYear(findUserEntity, CommonUtil.getYear()).orElseThrow(() -> new CommonException(ErrorCode.USER_TAX_INFO_NOT_FOUND));

        //한도액 계산
        BigDecimal totalPayment = findUserTaxInfoEntity.getTotalPayment();
        userTaxResultDto.setLimitAmount(limitAmountCalc(totalPayment).orElseThrow(() -> new CommonException(ErrorCode.LIMIT_AMOUNT_CALC_FAIL)));

        //공제액 계산
        BigDecimal totalAmountUsed = findUserTaxInfoEntity.getTotalAmountUsed();
        userTaxResultDto.setDeductible(deductibleCalc(totalAmountUsed, findUserTaxInfoEntity.getIncomeCls()).orElseThrow(() -> new CommonException(ErrorCode.DEDUCTIBLE_CALC_FAIL)));

        //환급액 계산
        userTaxResultDto.setRefundAmount(refundAmountCalc(userTaxResultDto.getDeductible(), userTaxResultDto.getLimitAmount()).orElseThrow(() -> new CommonException(ErrorCode.REFUND_AMOUNT_CALC_FAIL)));

        //환급액 저장 수정 여부 확인을 위하여 조회
        var findUserTaxResultDto = userTaxResultQueryRepository.findRefund(findUserEntity.getUserId(), CommonUtil.getYear());

        if(findUserTaxResultDto.isPresent()){
            //환급계산 결과 수정
            userTaxResultQueryRepository.updateUserTaxResult(
                    UserTaxResultDto.builder()
                            .id(findUserTaxResultDto.get().getId())
                            .year(CommonUtil.getYear())
                            .limitAmount(userTaxResultDto.getLimitAmount())
                            .deductible(userTaxResultDto.getDeductible())
                            .refundAmount(userTaxResultDto.getRefundAmount())
                            .build()
            );
        }else{
            //환급계산 결과 저장
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

        //환급계산 결과 조회
        var findRefundEntity = userTaxResultQueryRepository.findRefund(findUserEntity.getUserId(), CommonUtil.getYear()).orElseThrow(() -> new CommonException(ErrorCode.USER_TAX_RESULT_NOT_FOUND));
        
        //조회된 환급계산 결과 정보 기준으로 json 객체 데이터 반환
        return setRefundJson(findRefundEntity);
    }

    /**
     * 한도액 계산
     * */
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
                            .multiply(RefundCalcEnum.CALC_DECIMAL_0_008.getAmount())
            );

            return Optional.of(refundAmount.compareTo(new BigDecimal(660000)) == -1 ?
                    RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_66.getAmount() :
                    refundAmount);
        }
        //총급여액 7,000만원 초과
        else if(totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())  == 1){
            BigDecimal refundAmount = RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_66.getAmount().subtract(
                    totalPayment.subtract(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())
                            .multiply(RefundCalcEnum.CALC_DECIMAL_0_5.getAmount())
            );

            return Optional.of(refundAmount.compareTo(RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_50.getAmount()) == -1 ?
                    RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_50.getAmount() :
                    refundAmount);
        }

        return Optional.empty();
    }

    /**
     * 공제액 계산
     * */
    public Optional<BigDecimal> deductibleCalc(BigDecimal totalAmountUsed, String incomeCls){

        //소수점 버림
        totalAmountUsed = totalAmountUsed.setScale(0, RoundingMode.DOWN);

        //산출세액이 1,300,000만원 보다 작을경우
        if (totalAmountUsed.compareTo(RefundCalcEnum.CALCULATED_TAX_130.getAmount()) == -1) {
            return Optional.of(totalAmountUsed.multiply(RefundCalcEnum.CALC_DECIMAL_0_55.getAmount()));
        }

        //산출세액이 1,300,000만원 초과일 경우
        else if (totalAmountUsed.compareTo(RefundCalcEnum.CALCULATED_TAX_130.getAmount()) == 1) {
            return Optional.of(RefundCalcEnum.CALCULATED_TAX_71_5.getAmount().add(totalAmountUsed.subtract(
                    RefundCalcEnum.CALCULATED_TAX_130.getAmount()
            ).multiply(RefundCalcEnum.CALC_DECIMAL_0_3.getAmount())));
        }
        return Optional.empty();
    }

    /**
     * 환급액 계산
     * */
    public Optional<BigDecimal> refundAmountCalc(BigDecimal deductible, BigDecimal limitAmount){

        //공제액, 한도액의 값이 있을시
        if((null != deductible && 0 < deductible.intValue()) && (null != limitAmount && 0 < limitAmount.intValue())){
            return Optional.of(deductible.min(limitAmount));
        }

        return Optional.empty();
    }

    /**
     * 유저 세무정보에 대한 환급 계산 결과 JSONObject 세팅
     * */
    public JSONObject setRefundJson(UserTaxResultApiDto userTaxResultApiDto){
        JSONObject result = new JSONObject();

        result.put("이름", userTaxResultApiDto.getName());
        result.put("한도", CommonUtil.amountFormat(userTaxResultApiDto.getLimitAmount()));
        result.put("공제액", CommonUtil.amountFormat(userTaxResultApiDto.getDeductible()));
        result.put("환급액", CommonUtil.amountFormat(userTaxResultApiDto.getRefundAmount()));

        return result;
    }


}
