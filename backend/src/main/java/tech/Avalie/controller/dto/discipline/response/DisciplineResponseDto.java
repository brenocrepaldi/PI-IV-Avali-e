package tech.Avalie.controller.dto.discipline.response;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record DisciplineResponseDto (String id, String name, LocalTime start_time, LocalTime end_time, List<String> days_week, boolean active){
}
