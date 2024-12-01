package tech.Avalie.controller.dto.discipline.request;

import org.bson.types.ObjectId;

import java.time.LocalTime;
import java.util.List;

public record CreateDisciplineDto(String name, LocalTime start_time, LocalTime end_time, List<String> days_week,boolean active, ObjectId course){ }
