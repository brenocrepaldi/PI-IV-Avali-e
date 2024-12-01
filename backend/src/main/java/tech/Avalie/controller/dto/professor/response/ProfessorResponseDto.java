package tech.Avalie.controller.dto.professor.response;

import java.util.List;

public record ProfessorResponseDto(String id, String name, String email, String ra, List<String> disciplines, boolean active){
}
