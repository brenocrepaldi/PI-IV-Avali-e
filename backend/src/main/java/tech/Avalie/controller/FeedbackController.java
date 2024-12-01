package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.controller.dto.course.response.CourseResponseDto;
import tech.Avalie.controller.dto.feedback.request.FeedbackRequestDto;
import tech.Avalie.controller.dto.feedback.response.FeedbackResponseDto;
import tech.Avalie.entities.Course;
import tech.Avalie.entities.Disciplines;
import tech.Avalie.entities.Feedback;
import tech.Avalie.entities.Student;
import tech.Avalie.repository.DisciplineRepository;
import tech.Avalie.repository.FeedbackRepository;
import tech.Avalie.repository.StudentRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackRepository feedbackRepository;
    private final StudentRepository studentRepository;
    private final DisciplineRepository disciplineRepository;

    public FeedbackController(FeedbackRepository feedbackRepository, StudentRepository studentRepository, DisciplineRepository disciplineRepository) {
        this.feedbackRepository = feedbackRepository;
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<FeedbackResponseDto> newFeedback(@RequestBody FeedbackRequestDto dto) throws Exception {
        try {
            ObjectId studentId = new ObjectId(dto.student());
            ObjectId disciplineId = new ObjectId(dto.discipline());
            var studentOptional = studentRepository.findById(studentId);
            var disciplineOptional = disciplineRepository.findById(disciplineId);
            if (studentOptional.isEmpty() || disciplineOptional.isEmpty()) {
                return ResponseEntity.unprocessableEntity().build();
            }
            Feedback newFeedback = new Feedback();
            newFeedback.setText(dto.text());
            newFeedback.setStudent(dto.student());
            newFeedback.setDiscipline(dto.discipline());
            newFeedback.setDate(dto.date());
            newFeedback.setNote(dto.note());
            feedbackRepository.save(newFeedback);
            return ResponseEntity.ok(new FeedbackResponseDto(newFeedback.getId().toString(), newFeedback.getText(),
                    newFeedback.getStudent(), newFeedback.getDiscipline(), newFeedback.getDate(), newFeedback.getNote()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<FeedbackResponseDto>> findAll() throws Exception {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();
            List<FeedbackResponseDto> feedbackResponseDtos = feedbacks.stream()
                    .map(feedback -> new FeedbackResponseDto(
                            feedback.getId().toString(),
                            feedback.getText(),
                            feedback.getStudent(),
                            feedback.getDiscipline(),
                            feedback.getDate(),
                            feedback.getNote()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(feedbackResponseDtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByStudent")
    public ResponseEntity<List<Feedback>> findByStudent(@RequestParam String studentId) throws Exception {
        try {
            ObjectId idStudent = new ObjectId(studentId);
            Optional<Student> studentOptional = studentRepository.findById(idStudent);

            Optional<List<Feedback>> feedbackOption = feedbackRepository.findByStudent(studentId);

            if (feedbackOption.isEmpty()) {
                return ResponseEntity.unprocessableEntity().build();
            }

            List<Feedback> feedbacks = feedbackOption.get();
            return ResponseEntity.ok().body(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByDiscipline")
    public ResponseEntity<List<Feedback>> findByDiscipline(@RequestParam String disciplineId) throws Exception {
        try {
            ObjectId IdDiscipline = new ObjectId(disciplineId);
            Optional<Disciplines> discipline = disciplineRepository.findById(IdDiscipline);
            if(discipline.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            Optional<List<Feedback>> feedbacksOptional = feedbackRepository.findByDiscipline(disciplineId);
            if (feedbacksOptional.isEmpty()) {
                return ResponseEntity.unprocessableEntity().build();
            }
            return ResponseEntity.ok().body(feedbacksOptional.get());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/findByNote")
    public ResponseEntity<List<Feedback>> findByNote(@RequestParam int note)throws Exception{
        try{
            String nameRegex = "^" + note + ".*";
            Optional<List<Feedback>> noteOption= feedbackRepository.findByNote(Integer.parseInt(nameRegex));
            if(noteOption.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            List<Feedback> feedbacks = noteOption.get().stream()
                    .map(newStudent -> new Feedback(
                            newStudent.getId().toString(),
                            newStudent.getText(),
                            newStudent.getStudent(),
                            newStudent.getDiscipline(),
                            newStudent.getNote(),
                            newStudent.getDate()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok().body(feedbacks);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/findByDate")
    public ResponseEntity<List<Feedback>> findByName(@RequestParam Date date)throws Exception{
        try{
            String nameRegex = "^" + date + ".*";
            Optional<List<Feedback>> dateOption= feedbackRepository.findByDate(nameRegex);
            if(dateOption.isEmpty()){
                return ResponseEntity.unprocessableEntity().build();
            }
            List<Feedback> feedbacks = dateOption.get().stream()
                    .map(newStudent -> new Feedback(
                            newStudent.getId().toString(),
                            newStudent.getText(),
                            newStudent.getStudent(),
                            newStudent.getDiscipline(),
                            newStudent.getNote(),
                            newStudent.getDate()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok().body(feedbacks);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
