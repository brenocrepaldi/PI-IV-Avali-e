package tech.Avalie.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Document(collection = "professor")
public class Professor {
    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String password;
    private int    access_level;
    private String ra;
    private List<String> disciplines;
    private boolean active;

    public Professor(ObjectId id, String name,int access_level, String email, String password, String ra, List<String> disciplines, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.access_level = access_level;
        this.ra = ra;
        this.disciplines = disciplines;
        this.active = active;
    }

    public Professor() {
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRa() {
        return ra;
    }

    public List<String> getDisciplines() {
        return disciplines;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public void setDisciplines(List<String> disciplines) {
        this.disciplines = disciplines;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAccess_level() {
        return access_level;
    }

    public void setAccess_level(int access_level) {
        this.access_level = access_level;
    }

    public boolean isLoginIsCorrect(String rawPassword, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(rawPassword, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return access_level == professor.access_level && active == professor.active && Objects.equals(id, professor.id) && Objects.equals(name, professor.name) && Objects.equals(email, professor.email) && Objects.equals(password, professor.password) && Objects.equals(ra, professor.ra) && Objects.equals(disciplines, professor.disciplines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, access_level, ra, disciplines, active);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", access_level=" + access_level +
                ", ra='" + ra + '\'' +
                ", disciplines=" + disciplines +
                ", active=" + active +
                '}';
    }
}

