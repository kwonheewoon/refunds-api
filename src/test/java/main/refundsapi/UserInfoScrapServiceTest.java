package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class UserInfoScrapServiceTest {

    @Autowired
    private WebClient webClient;

    @Test
    void 유저_정보_스크랩(){
        var result = webClient.post()
                .uri("https://codetest.3o3.co.kr/v1/scrap")
                .body(Mono.just(UserDto.builder().name("김둘리").regNo("921108-1582816").build()), UserDto.class)
                .retrieve()
                .bodyToMono(String.class);

        log.info("데이타 : {}", result);
    }
}
