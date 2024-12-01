package tech.Avalie.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "feedback")
public class Feedback {
    @Id
    private ObjectId id;
    private String text;
    private String course;
    private String student;
    private String discipline;
    private int note;
    private Date date;

    public Feedback() {
    }

    public Feedback(String text, String course, String student, String discipline, int note, Date date) {
        this.text = text;
        this.course = course;
        this.student = student;
        this.discipline = discipline;
        this.note = note;
        this.date = date;
    }

    public String getId() {
        return id.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", course='" + course + '\'' +
                ", student='" + student + '\'' +
                ", discipline='" + discipline + '\'' +
                ", note=" + note +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return note == feedback.note && Objects.equals(id, feedback.id) && Objects.equals(text, feedback.text) && Objects.equals(course, feedback.course) && Objects.equals(student, feedback.student) && Objects.equals(discipline, feedback.discipline) && Objects.equals(date, feedback.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, course, student, discipline, note, date);
    }
}

