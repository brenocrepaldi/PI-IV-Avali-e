package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.controller.dto.professor.request.ProfessorRequestDto;
import tech.Avalie.controller.dto.professor.response.ProfessorResponseDto;
import tech.Avalie.entities.Disciplines;
import tech.Avalie.entities.Professor;
import tech.Avalie.repository.DisciplineRepository;
import tech.Avalie.repository.ProfessorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professor")
public class ProfessorController {
    private final ProfessorRepository professorRepository;
    private final DisciplineRepository disciplineRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ProfessorController(DisciplineRepository disciplineRepository,ProfessorRepository professorRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.professorRepository = professorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.disciplineRepository = disciplineRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ProfessorResponseDto> newProfessor(@RequestBody ProfessorRequestDto dto)throws Exception{
        try{
            var professorFromDB = professorRepository.findByEmail(dto.email());
            if(professorFromDB.isPresent()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Professor professor = new Professor();
            professor.setName(dto.name());
            professor.setEmail(dto.email());
            professor.setPassword(bCryptPasswordEncoder.encode(dto.password()));
            professor.setRa(dto.ra());
            professor.setDisciplines(dto.disciplines());
            professor.setActive(dto.active());
            professor.setAccess_level(1);
            professorRepository.save(professor);
            return ResponseEntity.ok(new ProfessorResponseDto(professor.getId(),professor.getName(),professor.getEmail()
                    ,professor.getRa(), professor.getDisciplines(),professor.isActive()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ProfessorResponseDto>> findAll() throws Exception {
        try {
            List<Professor> professors = professorRepository.findAll();

            List<ProfessorResponseDto> professorResponseDtos = professors.stream().map(professor -> {
                List<String> disciplineNames = professor.getDisciplines().stream()
                        .map(disciplineId -> disciplineRepository.findById(new ObjectId(disciplineId))
                                .map(Disciplines::getName)
                                .orElse("Disciplina não encontrada"))
                        .collect(Collectors.toList());
                return new ProfessorResponseDto(
                        professor.getId(),
                        professor.getName(),
                        professor.getEmail(),
                        professor.getRa(),
                        disciplineNames,
                        professor.isActive()
                );
            }).collect(Collectors.toList());

            return ResponseEntity.ok(professorResponseDtos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/findById")
    public ResponseEntity<ProfessorResponseDto> findById(@RequestParam String id) throws Exception {
        try {
            ObjectId professorId = new ObjectId(id);
            Optional<Professor> professorOptional = professorRepository.findById(professorId);

            if (professorOptional.isEmpty()) {
                return ResponseEntity.unprocessableEntity().build();
            }

            Professor professor = professorOptional.get();

            List<String> disciplineNames = professor.getDisciplines().stream()
                    .map(disciplineId -> disciplineRepository.findById(new ObjectId(disciplineId))
                            .map(Disciplines::getName)
                            .orElse("Disciplina não encontrada"))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ProfessorResponseDto(
                    professor.getId(),
                    professor.getName(),
                    professor.getEmail(),
                    professor.getRa(),
                    disciplineNames,
                    professor.isActive()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<ProfessorResponseDto>> findByName(@RequestParam String name)throws Exception{
        try{
            String nameRegex = "^" + name + ".*";
            Optional<List<Professor>> professorsOptional = professorRepository.findByName(nameRegex);
            if (professorsOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            List<ProfessorResponseDto> professorResponseDtos = professorsOptional.get().stream()
                    .map(professor -> new ProfessorResponseDto(
                            professor.getId(),
                            professor.getName(),
                            professor.getEmail(),
                            professor.getRa(),
                            professor.getDisciplines(),
                            professor.isActive()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(professorResponseDtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<ProfessorResponseDto> findByEmail(@RequestParam String email)throws Exception{
        try{
            Optional<Professor> professorOptional = professorRepository.findByEmail(email);
            if(professorOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Professor professor = professorOptional.get();
            return ResponseEntity.ok(new ProfessorResponseDto(professor.getId(),professor.getName(),professor.getEmail()
                    , professor.getRa(), professor.getDisciplines(),professor.isActive()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByDiscipline")
    public ResponseEntity<List<ProfessorResponseDto>> findByDiscipline(@RequestParam String disciplines) throws Exception{
        try{
            ObjectId disciplineId = new ObjectId(disciplines);
            Optional<Disciplines> disciplinesOptional = disciplineRepository.findById(disciplineId);
            if(disciplinesOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Optional<List<Professor>> professorOptional = professorRepository.findByDisciplines(disciplines);
            if(professorOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            List<ProfessorResponseDto> professorResponseDtos = professorOptional.get().stream()
                    .map(professor -> new ProfessorResponseDto(
                            professor.getId(),
                            professor.getName(),
                            professor.getEmail(),
                            professor.getRa(),
                            professor.getDisciplines(),
                            professor.isActive()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(professorResponseDtos);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestParam String id)throws Exception{
        try{
            ObjectId professorId = new ObjectId(id);
            Optional<Professor> professorOptional = professorRepository.findById(professorId);
            if(professorOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            Professor professorDelete = professorOptional.get();
            professorDelete.setActive(false);
            professorRepository.save(professorDelete);
            return ResponseEntity.ok("Student: " + professorDelete.getName() + " deleted.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("updateById")
    public ResponseEntity<ProfessorResponseDto> updateById(@RequestParam String id, @RequestBody ProfessorRequestDto dto) throws Exception {
        try {
            ObjectId professorId = new ObjectId(id);
            Optional<Professor> professorOptional = professorRepository.findById(professorId);
            if (professorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Professor professor = professorOptional.get();
            if (dto.name() != null) professor.setName(dto.name());
            if (dto.email() != null) professor.setEmail(dto.email());
            if (dto.ra() != null) professor.setRa(dto.ra());
            if (dto.disciplines() != null) professor.setDisciplines(dto.disciplines());
            if (dto.active() != professor.isActive()){
                professor.setActive(dto.active());
            }
            professorRepository.save(professor);
            return ResponseEntity.ok(new ProfessorResponseDto(professor.getId(),professor.getName(),professor.getEmail()
                    , professor.getRa(), professor.getDisciplines(),professor.isActive()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
