package tech.Avalie.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Document(collection = "students")
public class Student {
    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String password;
    private int    access_level;
    private String telephone;
    private String ra;
    private String course;
    private String schedule;
    private Boolean active;


    public Student(ObjectId id,String name,int access_level, String ra, String email, String telephone, String id_course, String id_schedule, Boolean active, String password) {
        this.id = id;
        this.name = name;
        this.ra = ra;
        this.email = email;
        this.access_level = access_level;
        this.telephone = telephone;
        this.course = id_course;
        this.schedule = id_schedule;
        this.active = active;
        this.password = password;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getAccess_level() {
        return access_level;
    }

    public void setAccess_level(int access_level) {
        this.access_level = access_level;
    }

    public Student() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String id_course) {
        this.course = id_course;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginIsCorrect(String rawPassword, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(rawPassword, this.password);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", access_level=" + access_level +
                ", telephone='" + telephone + '\'' +
                ", ra='" + ra + '\'' +
                ", course='" + course + '\'' +
                ", schedule='" + schedule + '\'' +
                ", active=" + active +
                '}';
    }
}
