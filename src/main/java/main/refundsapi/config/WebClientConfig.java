package main.refundsapi.config;

import main.refundsapi.common_enum.CommonEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.create(CommonEnum.SCRAP_URL.getText());
    }
}
