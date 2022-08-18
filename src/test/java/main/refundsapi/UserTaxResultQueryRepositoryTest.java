package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.util.SeedUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@Slf4j
public class UserTaxResultQueryRepositoryTest {
    private final byte[] pbszUserKey = "testCrypt2020!@#".getBytes();
    private final byte[] pbszIV = "1234567890123456".getBytes();


    @Test
    public void SEED_Custom_암복호화_테스트() {
        // given
        String rawMessage = "860824-1655068";
        log.info("원본 데이터 =>" + rawMessage);

        // when
        String encryptedMessage = SeedUtil.encrypt(rawMessage);
        log.info("암호화 데이터 =>" + encryptedMessage);
        String decryptedMessage = SeedUtil.decrypt(encryptedMessage);
        log.info("복호화 데이터 =>" + decryptedMessage);

        // then
        Assertions.assertThat(rawMessage).isEqualTo(decryptedMessage);
        Assertions.assertThat(rawMessage).isNotEqualTo(encryptedMessage);
    }
}
