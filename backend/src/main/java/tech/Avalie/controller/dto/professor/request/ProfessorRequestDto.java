package tech.Avalie.controller.dto.professor.request;

import java.util.List;

public record ProfessorRequestDto(String name,String password, String email, String ra, List<String> disciplines,boolean active){
}
