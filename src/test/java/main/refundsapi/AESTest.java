package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.util.AESUtil;
import org.junit.jupiter.api.Test;

@Slf4j
public class AESTest {

    @Test
    void AES_암복호화테스트() throws Exception {
        String encData = AESUtil.encrypt("910411-1656116");
        log.info("data = {}", encData);

        log.info("data = {}", AESUtil.decrypt(encData));
    }
}
