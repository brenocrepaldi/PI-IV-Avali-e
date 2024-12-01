package tech.Avalie.controller.dto.auth.login.response;

public record LoginResponse(String id,String accessToken,int access_level, String refreshToken) {
}