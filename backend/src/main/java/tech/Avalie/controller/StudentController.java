package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.ServerValidation.ValidationClient;
import tech.Avalie.controller.dto.student.request.StudentRequestDto;
import tech.Avalie.controller.dto.student.response.StudentResponseDto;
import tech.Avalie.entities.Professor;
import tech.Avalie.entities.Student;
import tech.Avalie.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public StudentController(StudentRepository studentRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.studentRepository = studentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentResponseDto> newUser(@RequestBody StudentRequestDto dto) throws Exception {
        try {
            var studentFromDB = studentRepository.findByEmail(dto.email());
            if (studentFromDB.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
            if (!ValidationClient.validate(dto.email())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (!ValidationClient.validate(dto.telephone())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            var student = new Student();
            student.setName(dto.name());
            student.setRa(dto.ra());
            student.setEmail(dto.email());
            student.setTelephone(dto.telephone());
            student.setActive(dto.active());
            student.setCourse(dto.course());
            student.setSchedule(dto.schedule());
            student.setPassword(bCryptPasswordEncoder.encode(dto.password()));
            student.setAccess_level(0);
            studentRepository.save(student);
            return ResponseEntity.ok(new StudentResponseDto(student.getId(),student.getName(),student.getEmail(),
                    student.getTelephone(),student.getRa(),student.getCourse(),student.getSchedule()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<StudentResponseDto>> findAll()throws Exception{
        try {
            List<Student> students = studentRepository.findAll();
            List<StudentResponseDto> studentResponseDtos = students.stream()
                    .map(student -> new StudentResponseDto(
                            student.getId(),
                            student.getName(),
                            student.getEmail(),
                            student.getTelephone(),
                            student.getRa(),
                            student.getCourse(),
                            student.getSchedule()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(studentResponseDtos);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByCourse")
    public ResponseEntity<List<StudentResponseDto>> findByCourse(@RequestParam String course) throws Exception{
        try{
            ObjectId courseId = new ObjectId(course);
            Optional<List<Student>> studentsOptional = studentRepository.findByCourse(courseId);
            if(studentsOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            List<StudentResponseDto> studentResponseDtos = studentsOptional.get().stream()
                    .map(student -> new StudentResponseDto(
                            student.getId(),
                            student.getName(),
                            student.getEmail(),
                            student.getTelephone(),
                            student.getRa(),
                            student.getCourse(),
                            student.getSchedule()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok().body(studentResponseDtos);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByRa")
    public ResponseEntity<StudentResponseDto> findByRA(@RequestParam String ra) throws Exception{
        try{
            Optional<Student> studentOptional = studentRepository.findByRa(ra);
            if(studentOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Student student = studentOptional.get();
            return ResponseEntity.ok(new StudentResponseDto(student.getId(),student.getName(),student.getEmail(),
                    student.getTelephone(),student.getRa(),student.getCourse(),student.getSchedule()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Student>> findByName(@RequestParam String name)throws Exception{
        try{
            String regexName = "^" + name + ".*";
            Optional<List<Student>> students = studentRepository.findByName(regexName);
            if(students.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            return ResponseEntity.ok().body(students.get());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<StudentResponseDto> findById(@RequestParam String id)throws Exception{
        try{
            ObjectId studentId = new ObjectId(id);
            Optional<Student> studentOptional = studentRepository.findById(studentId);
            if(studentOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Student student = studentOptional.get();
            return ResponseEntity.ok(new StudentResponseDto(student.getId(),student.getName(),student.getEmail(),
                    student.getTelephone(),student.getRa(),student.getCourse(),student.getSchedule()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<StudentResponseDto> findByEmail(@RequestParam String email)throws Exception{
        try {
            Optional<Student> studentOptional = studentRepository.findByEmail(email);
            if (studentOptional.isEmpty()){return ResponseEntity.notFound().build();}
            Student student = studentOptional.get();
            return ResponseEntity.ok(new StudentResponseDto(student.getId(),student.getName(),student.getEmail(),
                    student.getTelephone(),student.getRa(),student.getCourse(),student.getSchedule()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestParam String id)throws Exception{
        try{
            ObjectId studentId = new ObjectId(id);
            Optional<Student> studentOptional = studentRepository.findById(studentId);
            if(studentOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            } else if (!studentOptional.get().getActive()){
                return ResponseEntity.ok("Student " + studentOptional.get().getName() + "already deleted.");
            }
            Student studentDelete = studentOptional.get();
            studentDelete.setActive(false);
            studentRepository.save(studentDelete);
            return ResponseEntity.ok("Student: "+studentDelete.getName()+" deleted.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("updateById")
    public ResponseEntity<StudentResponseDto> updateById(@RequestParam String id, @RequestBody StudentRequestDto dto) throws Exception {
        try {
            ObjectId studentId = new ObjectId(id);
            Optional<Student> studentOptional = studentRepository.findById(studentId);
            if (studentOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Student student = studentOptional.get();
            if (dto.name() != null) student.setName(dto.name());
            if (dto.email() != null) student.setEmail(dto.email());
            if (dto.telephone() != null) student.setTelephone(dto.telephone());
            if (dto.ra() != null) student.setRa(dto.ra());
            if (dto.course() != null) student.setCourse(dto.course());
            if (dto.schedule() != null) student.setSchedule(dto.schedule());
            if (student.getActive() != dto.active()){
                student.setActive(dto.active());
            }
            studentRepository.save(student);
            return ResponseEntity.ok(new StudentResponseDto(student.getId(),student.getName(),student.getEmail(),
                    student.getTelephone(),student.getRa(),student.getCourse(),student.getSchedule()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}
