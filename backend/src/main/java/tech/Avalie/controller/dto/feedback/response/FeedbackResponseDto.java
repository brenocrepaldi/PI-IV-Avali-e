package tech.Avalie.controller.dto.feedback.response;

import java.util.Date;

public record FeedbackResponseDto(String id,String text, String student, String discipline, Date date, int note) {
}
