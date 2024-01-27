package main.refundsapi.taxinfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.user.enums.UserCode;
import main.refundsapi.user.domain.dto.UserSignupDto;
import main.refundsapi.user.domain.entity.User;
import main.refundsapi.user.exception.UserException;
import main.refundsapi.user.repository.UserRepository;
import main.refundsapi.scrap.domain.dto.ScrapResponseDto;
import main.refundsapi.scrap.enums.ScrapCode;
import main.refundsapi.scrap.exception.ScrapException;
import main.refundsapi.taxinfo.domain.dto.TaxCalculator;
import main.refundsapi.taxinfo.domain.dto.TaxInfoDto;
import main.refundsapi.taxinfo.domain.entity.TaxInfo;
import main.refundsapi.taxinfo.repository.TaxInfoRepository;
import main.refundsapi.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaxInfoService {

    private final TaxInfoRepository taxInfoRepository;

    private final UserRepository userRepository;

    @Value("${scrap.host}")
    private String host;

    /**
     * 회원 세무정보 scrap
     */
    @Transactional
    public ScrapResponseDto findScrap() {

        var findUser = userRepository.findByUserId(SecurityUtil.getCurrentUserId().orElseThrow(() -> new UserException(UserCode.USER_NOT_FOUND)))
                .orElseThrow(() -> new UserException(UserCode.USER_NOT_FOUND));

        ScrapResponseDto scrapResponseDto = getScrapApi(findUser.getName(), findUser.getRegNo());

        return saveTaxInfo(scrapResponseDto, findUser);
    }

    /**
     * 회원 세무정보 scrap api 호출
     */
    public ScrapResponseDto getScrapApi(String name, String regNo) {
        ResponseEntity<ScrapResponseDto> response = RestClient.create(host).post()
                .uri("/v2/scrap")
                .body(UserSignupDto.builder().name(name).regNo(regNo).build())
                .retrieve()
                .toEntity(ScrapResponseDto.class);

        return response.getBody();
    }

    /**
     * 회원 세무정보 scrap
     */
    public ScrapResponseDto saveTaxInfo(ScrapResponseDto scrapResponseDto, User findUser){
        if(null == scrapResponseDto){
            throw new ScrapException(ScrapCode.SCRAP_FAIL);
        }

        TaxInfoDto taxInfoDto = TaxCalculator.calculateTaxInfo(scrapResponseDto.data());

        taxInfoRepository.save(TaxInfo.builder()
                .user(findUser)
                    .cta(taxInfoDto.cta())
                    .eitca(taxInfoDto.eitca())
                    .sptda(taxInfoDto.sptda())
                    .stda(taxInfoDto.stda())
                    .rptda(taxInfoDto.rptda())
                .build());

        return scrapResponseDto;
    }


}
