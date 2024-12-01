package tech.Avalie.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tech.Avalie.entities.Schedule;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, ObjectId> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Schedule>> findByName(String name);

    Optional<List<Schedule>> findByDisciplines(String disciplines);
}
