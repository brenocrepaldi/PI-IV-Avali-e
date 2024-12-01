package tech.Avalie.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "disciplines")
public class Disciplines {
    @Id
    private ObjectId id;
    private String name;
    @Field("start_time")
    private LocalTime startTime;
    @Field("end_time")
    private LocalTime endTime;
    private List<String> days_week;
    private boolean active;
    private ObjectId course;

    public Disciplines(ObjectId id, String name, LocalTime startTime, LocalTime endTime, List<String> days_week, boolean active) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days_week = days_week;
        this.active = active;
    }

    public Disciplines(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplines that = (Disciplines) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(days_week, that.days_week) && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startTime, endTime, days_week, active);
    }

    @Override
    public String toString() {
        return "Disciplines{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start_time=" + startTime +
                ", end_time=" + endTime +
                ", days_week=" + days_week +
                ", active=" + active +
                ", couse=" + course +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse(){
        return this.course.toString();
    }

    public void setCourse(String course){
        ObjectId courseId = new ObjectId(course);
        this.course = courseId;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDays_week(List<String> days_week) {
        this.days_week = days_week;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public List<String> getDays_week() {
        return days_week;
    }

    public boolean getActive() {
        return active;
    }
}
