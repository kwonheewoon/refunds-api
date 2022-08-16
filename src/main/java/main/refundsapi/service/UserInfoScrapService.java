package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.TaxInfoEnum;
import main.refundsapi.dto.UserDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.util.SecurityUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserInfoScrapService {

    private final WebClient webClient;

    private final UserRepository userRepository;

    private final UserTaxInfoRepository userTaxInfoRepository;

    public JSONObject findScrap() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        var findUserEntity = SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                );


        String scrapResult = webClient.post()
                .uri("https://codetest.3o3.co.kr/v1/scrap")
                .body(Mono.just(UserDto.builder().name(findUserEntity.getName()).regNo(findUserEntity.getRegNo()).build()), UserDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //스크랩 json body 파싱
        JSONObject parseResult = (JSONObject) jsonParser.parse(scrapResult);

        //data 항목 조회
        JSONObject data = (JSONObject)parseResult.get("data");

        //jsonList 항목 조회
        JSONObject jsonList = (JSONObject)data.get("jsonList");



        List<JSONArray> scrapJsonList = new ArrayList<>();

        //scrap001 항목 조회
        //scrap001 add
        scrapJsonList.add((JSONArray)jsonList.get("scrap001"));

        //scrap002 항목 조회
        //scrap002 add
        scrapJsonList.add((JSONArray)jsonList.get("scrap002"));

        userTaxInfoSave(scrapJsonList, findUserEntity);

        /*log.info("스크랩1 : {}", scrap001.get(0));
        log.info("스크랩2 : {}", scrap002.get(0));*/

        return parseResult;
    }

    public UserTaxInfoEntity userTaxInfoSave(List<JSONArray> scrapJsonList, UserEntity findUserEntity){
        return userTaxInfoRepository.save(
                UserTaxInfoEntity.builder()
                .userEntity(findUserEntity)
                .totalPayment(new BigDecimal(((JSONObject)scrapJsonList.get(0).get(0)).get("총지급액").toString().replaceAll("\\.", ",").replaceAll(",", "")))
                .totalAmountUsed(new BigDecimal(((JSONObject)scrapJsonList.get(1).get(0)).get("총사용금액").toString().replaceAll(",", "")))
                .incomeCls(TaxInfoEnum.searchCode((String) ((JSONObject)scrapJsonList.get(1).get(0)).get("소득구분")))
                .build()
        );
    }


}
