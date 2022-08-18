package main.refundsapi;


import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.TaxInfoCodeEnum;
import main.refundsapi.dto.UserTaxInfoDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import main.refundsapi.repository.*;
import main.refundsapi.service.TaxInfoScrapWebClientService;
import main.refundsapi.util.CommonUtil;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserTaxInfoTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaxInfoRepository userTaxInfoRepository;

    @Mock
    private UserTaxInfoQueryRepository userTaxInfoQueryRepository;

    @Mock
    private TaxInfoScrapWebClientService taxInfoScrapWebClientService;

    @Nested
    @DisplayName("scrap에서 반환된 JSON 데이터 파싱")
    class ScrapJsonParsing {

        private JSONObject parseResult;

        @BeforeEach
        void save_setup() throws ParseException {
            String scrapResult = "{\n" +
                    "    \"data\": {\n" +
                    "        \"jsonList\": {\n" +
                    "            \"scrap004\": [\n" +
                    "                {\n" +
                    "                    \"수임된세무사\": \"\",\n" +
                    "                    \"수임동의여부\": \"false\",\n" +
                    "                    \"수임된세무사연락처\": \"\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"scrap003\": [\n" +
                    "                {\n" +
                    "                    \"주택소지여부\": \"false\",\n" +
                    "                    \"주택청약가입여부\": \"true\",\n" +
                    "                    \"주택청약납입금\": \"240,000\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"scrap002\": [\n" +
                    "                {\n" +
                    "                    \"총사용금액\": \"1,000,000\",\n" +
                    "                    \"소득구분\": \"산출세액\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"scrap001\": [\n" +
                    "                {\n" +
                    "                    \"소득내역\": \"급여\",\n" +
                    "                    \"총지급액\": \"24,000.000\",\n" +
                    "                    \"업무시작일\": \"2018.09.03\",\n" +
                    "                    \"기업명\": \"(주)고길동\",\n" +
                    "                    \"이름\": \"김둘리\",\n" +
                    "                    \"지급일\": \"2020.10.02\",\n" +
                    "                    \"업무종료일\": \"2018.10.02\",\n" +
                    "                    \"주민등록번호\": \"921108-1582816\",\n" +
                    "                    \"소득구분\": \"근로소득(연간)\",\n" +
                    "                    \"사업자등록번호\": \"010-44-55589\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"errMsg\": \"\",\n" +
                    "            \"company\": \"삼쩜삼\",\n" +
                    "            \"svcCd\": \"test01\",\n" +
                    "            \"userId\": \"2\"\n" +
                    "        },\n" +
                    "        \"appVer\": \"2021112501\",\n" +
                    "        \"hostNm\": \"jobis-codetest\",\n" +
                    "        \"workerResDt\": \"2022-08-17T16:40:44.337274\",\n" +
                    "        \"workerReqDt\": \"2022-08-17T16:40:44.337338\"\n" +
                    "    },\n" +
                    "    \"errors\": {},\n" +
                    "    \"status\": \"success\"\n" +
                    "}";
            JSONParser jsonParser = new JSONParser();
            parseResult = (JSONObject) jsonParser.parse(scrapResult);
        }

        @Test
        void 유저_세무정보_scrap_데이터_status_체크() throws ParseException {

            given(taxInfoScrapWebClientService.findScrap(anyString(), anyString())).willReturn(parseResult);

            var resultScrapJson = taxInfoScrapWebClientService.findScrap("홍길동", "860824-1655068");


            //스크랩 JSON 데이터 NULL 여부
            assertNotNull(resultScrapJson);
            //스크랩 JSON status 상태 체크 sucess
            Assertions.assertThat(CommonEnum.STATUS_SUCCESS.getName()).isEqualTo(resultScrapJson.get("status"));

            verify(taxInfoScrapWebClientService).findScrap("홍길동", "860824-1655068");

        }

        @Test
        void 유저_세무정보_scrap_데이터_항목별_값_검증() throws ParseException {

            //given
            List<JSONArray> scrapJsonList = new ArrayList<>();
            Map<String, JSONArray> scrapJsonMap = new HashMap<>();
            JSONObject scrapJsonData = parseResult;


            //when

            //data 항목 조회
            JSONObject data = (JSONObject) parseResult.get("data");

            //jsonList 항목 조회
            JSONObject jsonList = (JSONObject) data.get("jsonList");

            //scrap001 항목 조회
            //scrap001 put
            scrapJsonMap.put("scrap001", (JSONArray) jsonList.get("scrap001"));

            //scrap002 항목 조회
            //scrap002 put
            scrapJsonMap.put("scrap002", (JSONArray) jsonList.get("scrap002"));


            //then
            assertNotNull(data);
            assertNotNull(jsonList);
            assertNotNull(scrapJsonMap.get("scrap001"));
            assertNotNull(scrapJsonMap.get("scrap002"));
            Assertions.assertThat(scrapJsonMap.get("scrap001").size()).isEqualTo(1);
            Assertions.assertThat(((JSONObject)scrapJsonMap.get("scrap001").get(0)).get("총지급액")).isEqualTo("24,000.000");
            Assertions.assertThat(scrapJsonMap.get("scrap002").size()).isEqualTo(1);

        }
    }


    @Nested
    @DisplayName("유저 세무정보 저장 로직")
    class UserTaxInfoSave {

        private UserTaxInfoEntity userTaxInfoEntity;

        private UserTaxInfoEntity findUserTaxInfoEntity;

        private UserTaxInfoDto userTaxInfoDto;

        private UserEntity userEntity;

        @BeforeEach
        void save_setup() throws ParseException {
            userEntity = UserEntity.builder().id(1L).build();

            userTaxInfoDto = UserTaxInfoDto.builder()
                    .id(1L)
                    .year(CommonUtil.getYear())
                    .totalPayment(new BigDecimal(94666.666))
                    .totalAmountUsed(new BigDecimal(1333333.333))
                    .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                    .build();

            userTaxInfoEntity = UserTaxInfoEntity.builder()
                    .userEntity(userEntity)
                    .year(CommonUtil.getYear())
                    .totalPayment(new BigDecimal(94666.666))
                    .totalAmountUsed(new BigDecimal(1333333.333))
                    .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                    .build();

            findUserTaxInfoEntity = UserTaxInfoEntity.builder()
                    .userEntity(UserEntity.builder().id(1L).build())
                    .year(CommonUtil.getYear())
                    .totalPayment(new BigDecimal(94666.666))
                    .totalAmountUsed(new BigDecimal(1333333.333))
                    .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                    .build();
        }

        @Test
        void 유저_세무정보_저장(){
            given(userTaxInfoRepository.findByUserEntityAndYear(any(UserEntity.class),anyInt())).willReturn(Optional.of(findUserTaxInfoEntity));
            given(userTaxInfoQueryRepository.updateUserTaxInfo(any(UserTaxInfoDto.class))).willReturn(1L);
            given(userTaxInfoRepository.save(any(UserTaxInfoEntity.class))).willReturn(findUserTaxInfoEntity);


            var findUserTaxInfoData = userTaxInfoRepository.findByUserEntityAndYear(userEntity, 2022);

            //세무정보가 존재하면
            var updatedCount = userTaxInfoQueryRepository.updateUserTaxInfo(userTaxInfoDto);

            //세무정보가 존재하지 않으면
            var savedUserTaxInfoEntity = userTaxInfoRepository.save(userTaxInfoEntity);


            //세무정보가 존재여부 확인
            Assertions.assertThat(findUserTaxInfoData.isPresent()).isTrue();
            Assertions.assertThat(updatedCount).isEqualTo(1L);
            assertNotNull(savedUserTaxInfoEntity);
            Assertions.assertThat(savedUserTaxInfoEntity.getTotalPayment()).isEqualTo(new BigDecimal(94666.666));

            verify(userTaxInfoRepository).findByUserEntityAndYear(userEntity, 2022);
            verify(userTaxInfoQueryRepository).updateUserTaxInfo(userTaxInfoDto);
            verify(userTaxInfoRepository).save(userTaxInfoEntity);
        }
    }
}
