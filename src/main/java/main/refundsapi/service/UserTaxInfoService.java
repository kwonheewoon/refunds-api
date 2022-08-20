package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.TaxInfoCodeEnum;
import main.refundsapi.dto.UserTaxInfoDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoQueryRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.util.CommonUtil;
import main.refundsapi.util.SecurityUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserTaxInfoService {

    private final TaxInfoScrapWebClientService taxInfoScrapWebClientService;

    private final UserRepository userRepository;

    private final UserTaxInfoRepository userTaxInfoRepository;

    private final UserTaxInfoQueryRepository userTaxInfoQueryRepository;

    /**
     * 유저 세무정보 scrap
     * */
    @Transactional
    public JSONObject findScrap() throws ParseException {

        //JSON 데이터 저장용 Map 정의
        Map<String, JSONArray> scrapJsonMap = new HashMap<>();

        //SecurityContextHolder에서 저장된 userId로 유저 정보 조회
        var findUserEntity = SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                );

        //스크랩 json body 파싱
        JSONObject parseResult = taxInfoScrapWebClientService.findScrap(findUserEntity.getName(), findUserEntity.getRegNo());

        //스크랩 status : fail 바로 리턴
        if(CommonEnum.STATUS_FAIL.getName().equals(parseResult.get("status"))){
            return parseResult;
        }

        //data 항목 조회
        JSONObject data = (JSONObject)parseResult.get("data");

        //jsonList 항목 조회
        JSONObject jsonList = (JSONObject)data.get("jsonList");

        //scrap001 항목 조회
        //scrap001 put
        scrapJsonMap.put("scrap001", (JSONArray) jsonList.get("scrap001"));

        //scrap002 항목 조회
        //scrap002 put
        scrapJsonMap.put("scrap002", (JSONArray) jsonList.get("scrap002"));
        
        //유저 세무정보 저장
        userTaxInfoSave(scrapJsonMap, findUserEntity);


        return parseResult;
    }

    /**
     * 유저 세무정보 저장
     * */
    @Transactional
    public UserTaxInfoEntity userTaxInfoSave(Map<String, JSONArray> scrapJsonMap, UserEntity findUserEntity){
        
        //유저 세무정보 조회
        var findUserTaxInfoEntity = userTaxInfoRepository.findByUserEntityAndYear(findUserEntity, CommonUtil.getYear());
        
        //세무정보가 존재하면
        if(findUserTaxInfoEntity.isPresent()){

            //유저 세무정보 수정
            userTaxInfoQueryRepository.updateUserTaxInfo(
                    UserTaxInfoDto.builder()
                            .id(findUserTaxInfoEntity.get().getId())
                            .year(CommonUtil.getYear())
                            .totalPayment(new BigDecimal(((JSONObject)scrapJsonMap.get("scrap001").get(0)).get("총지급액").toString().replaceAll("\\.", ",").replaceAll(",", "")))
                            .totalAmountUsed(new BigDecimal(((JSONObject)scrapJsonMap.get("scrap002").get(0)).get("총사용금액").toString().replaceAll(",", "")))
                            .incomeCls(TaxInfoCodeEnum.searchCode((String) ((JSONObject)scrapJsonMap.get("scrap002").get(0)).get("소득구분")))
                            .build()
            );

            return userTaxInfoRepository.findByUserEntityAndYear(findUserEntity, CommonUtil.getYear()).orElseThrow(() -> new CommonException(ErrorCode.USER_TAX_INFO_NOT_FOUND));
        }
        
        //유저 세무정보 저장
        return userTaxInfoRepository.save(
                UserTaxInfoEntity.builder()
                .userEntity(findUserEntity)
                .year(CommonUtil.getYear())
                .totalPayment(new BigDecimal(((JSONObject)scrapJsonMap.get("scrap001").get(0)).get("총지급액").toString().replaceAll("\\.", ",").replaceAll(",", "")))
                .totalAmountUsed(new BigDecimal(((JSONObject)scrapJsonMap.get("scrap002").get(0)).get("총사용금액").toString().replaceAll(",", "")))
                .incomeCls(TaxInfoCodeEnum.searchCode((String) ((JSONObject)scrapJsonMap.get("scrap002").get(0)).get("소득구분")))
                .build()
        );


    }


}
