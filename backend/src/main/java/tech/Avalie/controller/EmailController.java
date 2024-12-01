package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.controller.dto.professor.response.ProfessorResponseDto;
import tech.Avalie.entities.Disciplines;
import tech.Avalie.entities.Schedule;
import tech.Avalie.entities.Student;
import tech.Avalie.repository.DisciplineRepository;
import tech.Avalie.repository.ProfessorRepository;
import tech.Avalie.repository.ScheduleRepository;
import tech.Avalie.repository.StudentRepository;
import tech.Avalie.services.EmailService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    private ScheduleRepository scheduleRepository;
    private DisciplineRepository disciplineRepository;
    private StudentRepository studentRepository;
    private ProfessorRepository professorRepository;

    public EmailController(ScheduleRepository scheduleRepository, DisciplineRepository disciplineRepository, StudentRepository studentRepository, ProfessorRepository professorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.disciplineRepository = disciplineRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String toEmail) {
        try {
            emailService.sendSimpleEmail(toEmail, "teste de envio de email", "Corpo do email.");
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar o email: " + e.getMessage());
        }
    }

    @PostMapping("/sendFeedback")
    public ResponseEntity<String> sendFeedback(@RequestParam String disciplineId){
        try {
            ObjectId idDiscipline = new ObjectId(disciplineId);
            Optional<Disciplines> disciplinesOptional = disciplineRepository.findById(idDiscipline);
            if(disciplinesOptional.isEmpty()){
                return ResponseEntity.unprocessableEntity().body("Discipline not found.");
            }
            Optional<List<Schedule>> optionalSchedules = scheduleRepository.findByDisciplines(disciplineId);
            if (optionalSchedules.isEmpty()){
                ResponseEntity.unprocessableEntity().body("Schedule not found.");
            }

            List<String> scheduleIds = optionalSchedules.get().stream()
                    .map(Schedule::getId)
                    .collect(Collectors.toList());

            Optional<List<Student>> optionalStudents = studentRepository.findByScheduleIn(scheduleIds);
            if(optionalStudents.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<String> studentEmails = optionalStudents.get().stream().map(Student::getEmail).collect(Collectors.toList());
            for (String email : studentEmails) {
                String feedbackDeepLink = "http://10.0.2.2/disciplina?disciplineId=" + disciplineId;
                String emailContent = "<p>Olá,</p>" +
                        "<p>Avalie sua aula de <strong>" + disciplinesOptional.get().getName() + "</strong>!</p>" +
                        "<p>Clique no link abaixo para dar sua nota:</p>" +
                        "<p><a href=\"" + feedbackDeepLink + "\">Avaliar aula</a></p>" +
                        "<p>Obrigado!</p>";

                emailService.sendHtmlEmail(email, "Avalie sua aula de " + disciplinesOptional.get().getName(), emailContent);
            }
            return ResponseEntity.ok("Email has been sent.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }
}
