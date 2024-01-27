package main.refundsapi.scrap.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseDto(
    @JsonProperty("jsonList")
    JsonListDto jsonListData,
    String appVer,
    String errMsg,
    String company,
    String svcCd,
    String hostNm,
    String workerResDt,
    String workerReqDt

){

}
