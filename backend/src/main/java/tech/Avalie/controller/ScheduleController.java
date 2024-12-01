package tech.Avalie.controller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.Avalie.entities.Schedule;
import tech.Avalie.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> newSchedule(@RequestBody Schedule schedule) throws Exception {
        try {
            if (scheduleRepository.findByName(schedule.getName()).isPresent()) {
                return ResponseEntity.unprocessableEntity().body("Schedule with this name already exists.");
            }
            Schedule item = new Schedule();
            schedule.setName(schedule.getName());
            schedule.setCourse(schedule.getCourse());
            schedule.setYear(schedule.getYear());
            schedule.setDisciplines(schedule.getDisciplines());
            schedule.setActive(schedule.getActive());
            scheduleRepository.save(item);
            return ResponseEntity.ok().body(item.toString());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Schedule>> findAll() throws Exception {
        try {
            List<Schedule> schedules = new ArrayList<>();
            schedules = scheduleRepository.findAll();
            return ResponseEntity.ok().body(schedules);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Schedule>> findByName(@RequestParam String name) throws Exception {
        try {
            String nameRegex = "^" + name + ".*";
            Optional<List<Schedule>> schedule = scheduleRepository.findByName(nameRegex);
            if (schedule.isEmpty()) {
                return ResponseEntity.unprocessableEntity().build();
            }
            return ResponseEntity.ok().body(schedule.get());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<Schedule> findById(@RequestParam String id) throws Exception {
        try {
            ObjectId scheduleId = new ObjectId(id);
            Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
            return ResponseEntity.ok().body(schedule.get());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestParam String id) throws Exception {
        try {
            ObjectId scheduleId = new ObjectId(id);
            Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);
            if (scheduleOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Schedule scheduleDelete = scheduleOptional.get();
            scheduleDelete.setActive(false);
            scheduleRepository.save(scheduleDelete);
            return ResponseEntity.ok("Course: " + scheduleOptional.get().getName() + " deleted.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
