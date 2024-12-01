package tech.Avalie.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "schedule")
public class Schedule {
    @Id
    private ObjectId id;
    private String name;
    private String course;
    private short year;

   private List<ObjectId> disciplines;
   private boolean active;

    public Schedule(ObjectId id, String name, String course, short year, List<ObjectId> disciplines, boolean active) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.year = year;
        this.disciplines = disciplines;
        this.active = active;
    }

    public Schedule() {
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public short getYear() {
        return year;
    }
    public boolean getActive(){return active;}

    public List<ObjectId> getDisciplines() {
        return disciplines;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public void setDisciplines(List<ObjectId> disciplines) {
        this.disciplines = disciplines;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", year=" + year +
                ", disciplines=" + disciplines +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return year == schedule.year && active == schedule.active && Objects.equals(id, schedule.id) && Objects.equals(name, schedule.name) && Objects.equals(course, schedule.course) && Objects.equals(disciplines, schedule.disciplines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, course, year, disciplines, active);
    }
}
