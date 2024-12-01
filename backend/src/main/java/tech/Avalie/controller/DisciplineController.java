package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.ServerValidation.ValidationClient;
import tech.Avalie.controller.dto.discipline.request.CreateDisciplineDto;
import tech.Avalie.controller.dto.discipline.response.DisciplineResponseDto;
import tech.Avalie.controller.dto.student.response.StudentResponseDto;
import tech.Avalie.entities.Disciplines;
import tech.Avalie.entities.Student;
import tech.Avalie.repository.DisciplineRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/disciplines")
public class DisciplineController {
   private final DisciplineRepository disciplineRepository;

   public DisciplineController(DisciplineRepository disciplineRepository){
       this.disciplineRepository = disciplineRepository;
   }

   @PostMapping("/register")
    public ResponseEntity<DisciplineResponseDto> newDiscipline(@RequestBody CreateDisciplineDto dto)throws Exception{
       try{
           var discipline = disciplineRepository.findByName(dto.name());
           if (!discipline.get().isEmpty()){
               return ResponseEntity.status(HttpStatus.CONFLICT).build();
           }

           Disciplines disciplines = new Disciplines();
           disciplines.setName(dto.name());
           disciplines.setStartTime(dto.start_time());
           disciplines.setEndTime(dto.end_time());
           disciplines.setDays_week(dto.days_week());
           disciplines.setActive(dto.active());
           disciplines.setCourse(dto.course().toString());
           disciplineRepository.save(disciplines);
           return ResponseEntity.ok(new DisciplineResponseDto(disciplines.getId()
                   ,disciplines.getName(),disciplines.getStartTime(),disciplines.getEndTime()
                   ,disciplines.getDays_week(),disciplines.getActive()));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }

   @GetMapping("/findAll")
    public ResponseEntity<List<DisciplineResponseDto>> findAll()throws Exception{
       try{
           List<Disciplines> disciplinesOptional = disciplineRepository.findAll();
           List<DisciplineResponseDto> disciplineResponseDtos = disciplinesOptional.stream()
                   .map(disciplines -> new DisciplineResponseDto(
                           disciplines.getId(),
                           disciplines.getName(),
                           disciplines.getStartTime(),
                           disciplines.getEndTime(),
                           disciplines.getDays_week(),
                           disciplines.getActive()
                   )).collect(Collectors.toList());
           return ResponseEntity.ok(disciplineResponseDtos);
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
   }

   @GetMapping("/findByName")
    public ResponseEntity<List<DisciplineResponseDto>> findByName(@RequestParam String name)throws Exception{
       try{
           String nameRegex = "^" + name + ".*";
           Optional<List<Disciplines>> disciplinesOptional = disciplineRepository.findByName(nameRegex);
           if(disciplinesOptional.isEmpty()){
               return ResponseEntity.unprocessableEntity().build();
           }
           List<DisciplineResponseDto> disciplineResponseDtos = disciplinesOptional.get().stream()
                   .map(disciplines -> new DisciplineResponseDto(
                           disciplines.getId(),
                           disciplines.getName(),
                           disciplines.getStartTime(),
                           disciplines.getEndTime(),
                           disciplines.getDays_week(),
                           disciplines.getActive()
                   )).collect(Collectors.toList());
           return ResponseEntity.ok(disciplineResponseDtos);
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
   }

   @GetMapping("/findById")
    public ResponseEntity<DisciplineResponseDto> findById(@RequestParam String id)throws Exception{
       try{
           ObjectId disciplineId = new ObjectId(id);
           Optional<Disciplines> disciplinesOptional = disciplineRepository.findById(disciplineId);
           if(disciplinesOptional.isEmpty()){
               return ResponseEntity.unprocessableEntity().build();
           }
           Disciplines disciplines = disciplinesOptional.get();
           return ResponseEntity.ok(new DisciplineResponseDto(disciplines.getId()
                   ,disciplines.getName(),disciplines.getStartTime(),disciplines.getEndTime()
                   ,disciplines.getDays_week(),disciplines.getActive()));
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
   }

   @GetMapping("/findByCourse")
   public ResponseEntity<List<DisciplineResponseDto>> findByCourse(@RequestParam String course)throws Exception{
       try{
           ObjectId courseId = new ObjectId(course);
           Optional<List<Disciplines>> disciplinesOptional = disciplineRepository.findByCourse(courseId);
           if(disciplinesOptional.isEmpty()){
               return ResponseEntity.notFound().build();
           }
           List<DisciplineResponseDto> disciplineResponseDtos = disciplinesOptional.get().stream()
                   .map(disciplines -> new DisciplineResponseDto(
                           disciplines.getId(),
                           disciplines.getName(),
                           disciplines.getStartTime(),
                           disciplines.getEndTime(),
                           disciplines.getDays_week(),
                           disciplines.getActive()
                   )).collect(Collectors.toList());
           return ResponseEntity.ok(disciplineResponseDtos);
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
   }

   @PostMapping("/findByTime")
   public ResponseEntity<List<DisciplineResponseDto>> findByTime(@RequestBody CreateDisciplineDto dto)throws Exception{
       try{
           var disciplinesOptional = disciplineRepository.findByStartTimeBetween(dto.start_time(),dto.end_time());
           if(disciplinesOptional.isEmpty()){
               return ResponseEntity.unprocessableEntity().build();
           }
           List<DisciplineResponseDto> disciplineResponseDtos = disciplinesOptional.get().stream()
                   .map(disciplines -> new DisciplineResponseDto(
                           disciplines.getId(),
                           disciplines.getName(),
                           disciplines.getStartTime(),
                           disciplines.getEndTime(),
                           disciplines.getDays_week(),
                           disciplines.getActive()
                   )).collect(Collectors.toList());
           return ResponseEntity.ok(disciplineResponseDtos);
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
   }

   @DeleteMapping("/deleteById")
   public ResponseEntity<String> deleteById(@RequestParam String id)throws Exception{
       try {
           ObjectId disciplineId = new ObjectId(id);
           Optional<Disciplines> disciplinesOptional = disciplineRepository.findById(disciplineId);
           if(disciplinesOptional.isEmpty()){
               return ResponseEntity.notFound().build();
           }
           Disciplines disciplineDelete = disciplinesOptional.get();
           disciplineDelete.setActive(false);
           disciplineRepository.save(disciplineDelete);
           return ResponseEntity.ok("Discipline: " +disciplineDelete.getName()+" Deleted." );
       } catch (Exception e) {
           return ResponseEntity.internalServerError().body("Server error: "+e.getMessage());
       }
   }

}
