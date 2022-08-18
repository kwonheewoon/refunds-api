package main.refundsapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.TaxInfoCodeEnum;
import main.refundsapi.dto.UserDto;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaxInfoScrapWebClientService {

    private final WebClient webClient;

    private final UserRepository userRepository;

    /**
     * 유저 세무정보 scrap
     * */
    @Transactional
    public JSONObject findScrap(String name, String regNo) throws ParseException {
        JSONParser jsonParser = new JSONParser();

        //조죄한 유저 정보 토대로 scrap uri에서 세무정보 요청
        String scrapResult = webClient.post()
                .uri(CommonEnum.SCRAP_URL.getText())
                .body(Mono.just(UserDto.builder().name(name).regNo(regNo).build()), UserDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //스크랩 body json 데이터 파싱
        JSONObject parseResult = (JSONObject) jsonParser.parse(scrapResult);

        return parseResult;
    }


}
