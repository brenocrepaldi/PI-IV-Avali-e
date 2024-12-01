package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.ServerValidation.ValidationClient;
import tech.Avalie.controller.dto.director.request.DirectorRequestDto;
import tech.Avalie.controller.dto.director.response.DirectorResponseDto;
import tech.Avalie.entities.Course;
import tech.Avalie.entities.Director;
import tech.Avalie.repository.CourseRepository;
import tech.Avalie.repository.DirectorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/director")
public class DirectorController {
    private final DirectorRepository directorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CourseRepository courseRepository;

    public DirectorController(CourseRepository courseRepository,DirectorRepository directorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.directorRepository = directorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<DirectorResponseDto> newDirector (@RequestBody DirectorRequestDto directorRequestDto)throws Exception{
        try{
            if(!ValidationClient.validate(directorRequestDto.email())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            var director = directorRepository.findByEmail(directorRequestDto.email());
            if(director.isPresent()){
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }

            ObjectId courseId = new ObjectId(directorRequestDto.course());
            var course = courseRepository.findById(courseId);
            if(course.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }

            Director newDirector = new Director();
            newDirector.setName(directorRequestDto.name());
            newDirector.setRa(directorRequestDto.ra());
            newDirector.setEmail(directorRequestDto.email());
            newDirector.setPassword(bCryptPasswordEncoder.encode(directorRequestDto.password()));
            newDirector.setActive(directorRequestDto.active());
            newDirector.setAccess_level(2);
            newDirector.setCourse(directorRequestDto.course());
            directorRepository.save(newDirector);
            return ResponseEntity.ok(new DirectorResponseDto(newDirector.getId(),newDirector.getName(),
                    newDirector.getRa(),newDirector.getEmail(),newDirector.getCourse()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DirectorResponseDto>> findAll()throws Exception{
        try {
            List<Director> directors = directorRepository.findAll();
            List<DirectorResponseDto> responseDtos = directors.stream()
                    .map(director -> new DirectorResponseDto(
                            director.getId(),
                            director.getName(),
                            director.getRa(),
                            director.getEmail(),
                            director.getCourse()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/findById")
    public ResponseEntity<DirectorResponseDto> findById(@RequestParam String id)throws Exception{
        try{
            ObjectId directorId = new ObjectId(id);
            Optional<Director> director = directorRepository.findById(directorId);
            if(director.isEmpty()){return ResponseEntity.unprocessableEntity().build();}
            Director directorResponse = director.get();
            ObjectId courseId = new ObjectId(directorResponse.getCourse());
            var course = courseRepository.findById(courseId);
            return ResponseEntity.ok(new DirectorResponseDto(directorResponse.getId(),directorResponse.getName()
                    ,directorResponse.getRa(),directorResponse.getEmail(),course.get().getName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<DirectorResponseDto>> findByName(@RequestParam String name) throws Exception {
        try {
            String nameRegex = "^" + name + ".*";
            Optional<List<Director>> directors = directorRepository.findByName(nameRegex);

            if (directors.isEmpty()) {
                return ResponseEntity.unprocessableEntity().build();
            }
            List<DirectorResponseDto> responseDtos = directors.get().stream()
                    .map(director -> new DirectorResponseDto(
                            director.getId(),
                            director.getName(),
                            director.getRa(),
                            director.getEmail(),
                            director.getCourse()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByRa")
    public ResponseEntity<DirectorResponseDto> findByRa(@RequestParam String ra)throws Exception{
        try {
            Optional<Director> directorOptional = directorRepository.findByRa(ra);
            if(directorOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Director director = directorOptional.get();
            return ResponseEntity.ok(new DirectorResponseDto(director.getId(),director.getName()
                    ,director.getRa(),director.getRa(),director.getCourse()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/findByEmail")
    public ResponseEntity<DirectorResponseDto> findByEmail(@RequestParam String email)throws Exception{
        try{
            Optional<Director> directorOptional = directorRepository.findByEmail(email);
            if(directorOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Director director = directorOptional.get();
            return ResponseEntity.ok(new DirectorResponseDto(director.getId(),director.getName(),director.getRa(),
                    director.getEmail(),director.getCourse()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByCourse")
    public ResponseEntity<DirectorResponseDto> findByCourse(@RequestParam String course)throws Exception{
        try {
            ObjectId courseid = new ObjectId(course);
            Optional<Course> courseOptional = courseRepository.findById(courseid);
            if(courseOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Optional<Director> directorOptional = directorRepository.findByCourse(course);
            if(directorOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Director director = directorOptional.get();
            return ResponseEntity.ok(new DirectorResponseDto(director.getId(),director.getName()
                    ,director.getRa(),director.getEmail(),director.getCourse()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestParam String id)throws Exception{
        try{
            ObjectId directorId = new ObjectId(id);
            Optional<Director> directorOptional = directorRepository.findById(directorId);
            if(directorOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            Director directorDelete = directorOptional.get();
            directorDelete.setActive(false);
            directorRepository.save(directorDelete);
            return ResponseEntity.ok("Student: "+ directorDelete.getName() +" deleted.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("updateById")
    public ResponseEntity<DirectorResponseDto> updateById(@RequestParam String id, @RequestBody DirectorRequestDto dto) throws Exception {
        try {
            ObjectId directorId = new ObjectId(id);
            Optional<Director> directorOptional = directorRepository.findById(directorId);
            if (directorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Director director = directorOptional.get();
            if (dto.name() != null) director.setName(dto.name());
            if (dto.email() != null) director.setEmail(dto.email());
            if (dto.ra() != null) director.setRa(dto.ra());
            if (dto.course() != null) director.setCourse(dto.course());
            if (dto.active() != director.isActive()){
                director.setActive(dto.active());
            }
            directorRepository.save(director);
            return ResponseEntity.ok(new DirectorResponseDto(director.getId(),director.getName(),director.getRa()
                    ,director.getEmail(),director.getCourse()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}
