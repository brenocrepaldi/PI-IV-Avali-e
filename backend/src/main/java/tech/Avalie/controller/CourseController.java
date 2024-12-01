package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.controller.dto.course.request.CourseRequestDto;
import tech.Avalie.controller.dto.course.response.CourseResponseDto;
import tech.Avalie.entities.Course;
import tech.Avalie.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<CourseResponseDto> newCourse(@RequestBody CourseRequestDto course)throws Exception{
        try {
            if (courseRepository.findByName(course.name()).isPresent()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Course newCourse = new Course();
            newCourse.setName(course.name());
            newCourse.setActive(course.active());
            courseRepository.save(newCourse);
            return ResponseEntity.ok(new CourseResponseDto(newCourse.getId(),newCourse.getName(),newCourse.isActive()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CourseResponseDto>> findAll()throws Exception{
        try{
            List<Course> courses = new ArrayList<>();
            courses = courseRepository.findAll();
            List<CourseResponseDto> courseResponseDtos = courses.stream()
                    .map(course -> new CourseResponseDto(
                            course.getId(),
                            course.getName(),
                            course.isActive()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok().body(courseResponseDtos);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/findByName")
    public ResponseEntity<List<CourseResponseDto>> findByName(@RequestParam String name)throws Exception{
        try{
            String nameRegex = "^" + name + ".*";
            Optional<List<Course>> courseOption= courseRepository.findByName(nameRegex);
            if(courseOption.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            List<CourseResponseDto> courseResponseDtos = courseOption.get().stream()
                    .map(course -> new CourseResponseDto(
                            course.getId().toString(),
                            course.getName(),
                            course.isActive()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok().body(courseResponseDtos);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<Course> findById(@RequestParam String id)throws Exception{
        try{
            ObjectId courseId = new ObjectId(id);
            Optional<Course> course = courseRepository.findById(courseId);
            return ResponseEntity.ok().body(course.get());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestParam String id)throws Exception{
        try{
            ObjectId courseId = new ObjectId(id);
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if(courseOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            Course courseDelete = courseOptional.get();
            courseDelete.setActive(false);
            courseRepository.save(courseDelete);
            return ResponseEntity.ok("Course: "+courseOptional.get().getName()+" deleted.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("updateById")
    public ResponseEntity<CourseResponseDto> updateById(@RequestParam String id, @RequestBody CourseRequestDto courseRequestDto) throws Exception {
        try {
            ObjectId courseId = new ObjectId(id);
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Course course = courseOptional.get();
            if (courseRequestDto.name() != null) course.setName(courseRequestDto.name());
            if (courseRequestDto.active() != course.isActive()) {
                course.setActive(courseRequestDto.active());
            }
                courseRepository.save(course);
                return ResponseEntity.ok(new CourseResponseDto(course.getId(),course.getName(),course.isActive()));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}
