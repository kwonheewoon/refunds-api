package main.refundsapi.scrap;

import main.refundsapi.scrap.domain.dto.*;

import java.util.Arrays;
import java.util.List;

public class ScrapResponseDtoFactory {

    // 홍길동 유저 스크랩 결과 dto factory method
    public static ScrapResponseDto hongResponse(){
        return new ScrapResponseDto("success",
                new ResponseDto(
                        new JsonListDto(
                                List.of(
                                        new SalaryInfoDto("급여", "60,000,000", "2020.10.02", "(주)활빈당", "홍길동", "2020.11.02", "2021.11.02", "860284-1655068", "근로소득(연간)", "012-34-5678")
                                ),
                                "3,000,000",
                                Arrays.asList(
                                        new IncomeDeductionDto("100,000", "보험료", null), // 12000
                                        new IncomeDeductionDto("200,000", "교육비", null), // 30000
                                        new IncomeDeductionDto("150,000", "기부금", null), // 22500
                                        new IncomeDeductionDto("4,400,000", "의료비", null),// -2040000
                                        new IncomeDeductionDto(null, "퇴직연금", "6,000,000") // 900000
                                )
                        ),
                        "2021112501",
                        "",
                        "삼쩜삼",
                        "test01",
                        "jobis-codetest",
                        "2022-08-16T06:27:35.160789",
                        "2022-08-16T06:27:35.160851"
                )
                , null);
    }

    // 김둘리 유저 스크랩 결과 dto factory method
    public static ScrapResponseDto kimResponse() {
        return new ScrapResponseDto("success",
                new ResponseDto(
                        new JsonListDto(
                                List.of(
                                        new SalaryInfoDto("급여", "30,000,000", "2018.09.03", "(주)고길동", "김둘리", "2020.10.02", "2021.10.02", "921108-1582816", "근로소득(연간)", "010-44-55589")
                                ),
                                "1,200,000",
                                Arrays.asList(
                                        new IncomeDeductionDto("100,000", "보험료", null), // 12000
                                        new IncomeDeductionDto("200,000", "교육비", null), // 30000
                                        new IncomeDeductionDto("150,000", "기부금", null), // 22500
                                        new IncomeDeductionDto("700,000", "의료비", null),// -2040000
                                        new IncomeDeductionDto(null, "퇴직연금", "1,333,333.333") // 900000
                                )
                        ),
                        "2021112501",
                        "",
                        "삼쩜삼",
                        "test01",
                        "jobis-codetest",
                        "2022-08-16T06:27:35.160789",
                        "2022-08-16T06:27:35.160851"
                )
                , null);
    }

        // 마징가 유저 스크랩 결과 dto factory method
        public static ScrapResponseDto mazingResponse(){
            return new ScrapResponseDto("success",
                    new ResponseDto(
                            new JsonListDto(
                                    List.of(
                                            new SalaryInfoDto("급여", "80,000,000", "2020.10.02", "(주)초합금", "마징가", "2020.11.02", "2021.11.02", "880601-2455116", "근로소득(연간)", "013-44-56689")
                                    ),
                                    "4,700,000.11",
                                    Arrays.asList(
                                            new IncomeDeductionDto("200,000", "보험료", null), // 12000
                                            new IncomeDeductionDto("200,000", "교육비", null), // 30000
                                            new IncomeDeductionDto("200,000", "기부금", null), // 22500
                                            new IncomeDeductionDto("5,000,000", "의료비", null),// -2040000
                                            new IncomeDeductionDto(null, "퇴직연금", "7,233,333.333") // 900000
                                    )
                            ),
                            "2021112501",
                            "",
                            "삼쩜삼",
                            "test01",
                            "jobis-codetest",
                            "2022-08-16T06:27:35.160789",
                            "2022-08-16T06:27:35.160851"
                    )
                    , null);
    }
    // 손오공 유저 스크랩 결과 dto factory method
    public static ScrapResponseDto sonResponse(){
        return new ScrapResponseDto("success",
                new ResponseDto(
                        new JsonListDto(
                                List.of(
                                        new SalaryInfoDto("급여", "50,000,000", "2020.10.02", "지구행성0-1", "손오공", "2020.11.02", "2021.11.02", "820326-2715702", "근로소득(연간)", "012-23-12345")
                                ),
                                "3,000,000",
                                Arrays.asList(
                                        new IncomeDeductionDto("160,000", "보험료", null), // 12000
                                        new IncomeDeductionDto("300,000", "교육비", null), // 30000
                                        new IncomeDeductionDto("200,000", "기부금", null), // 22500
                                        new IncomeDeductionDto("500,000", "의료비", null),// -2040000
                                        new IncomeDeductionDto(null, "퇴직연금", "3,000,000") // 900000
                                )
                        ),
                        "2021112501",
                        "",
                        "삼쩜삼",
                        "test01",
                        "jobis-codetest",
                        "2022-08-16T06:27:35.160789",
                        "2022-08-16T06:27:35.160851"
                )
                , null);
    }
}
