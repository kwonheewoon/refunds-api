package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.dto.UserDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TaxInfoScrapWebClientService {

    @Autowired
    private WebClient webClient;

    @Test
    void 유저_정보_스크랩_api_호출() throws ParseException {

        //JSON 데이터 저장용 Map 정의
        Map<String, JSONArray> scrapJsonMap = new HashMap<>();
        JSONParser jsonParser = new JSONParser();

                webClient.post()
                .uri("https://codetest.3o3.co.kr/v1/scrap")
                .body(Mono.just(UserDto.builder().name("김둘리").regNo("921108-1582816").build()), UserDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(scrapData -> {
                    //스크랩 body json 데이터 파싱
                    JSONObject parseResult = null;
                    try {
                        parseResult = (JSONObject) jsonParser.parse(scrapData);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //data 항목 조회
                    JSONObject data = (JSONObject)parseResult.get("data");

                    //jsonList 항목 조회
                    JSONObject jsonList = (JSONObject)data.get("jsonList");


                    assertNotNull(data);
                    assertNotNull(jsonList);
                    assertNotNull(jsonList.get("scrap001"));
                    assertNotNull(jsonList.get("scrap002"));
                });


    }
}
