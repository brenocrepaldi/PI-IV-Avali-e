package tech.Avalie.controller.dto.auth.refresh.response;

public record RefreshResponseDto(String id,String accessToken, int access_level, String refreshToken){
}
