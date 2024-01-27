package main.refundsapi.scrap.domain.dto;

public record ScrapResponseDto(
        String status,
        ResponseDto data,
        Object errors
) {
}
