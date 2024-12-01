package tech.Avalie.controller.dto.feedback.request;

import java.util.Date;

public record FeedbackRequestDto(String text, String student, String discipline, Date date, int note) {
}
