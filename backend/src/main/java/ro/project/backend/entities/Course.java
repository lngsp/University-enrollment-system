
package ro.project.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="course")
public class Course {
    @Id
    private String code; //course code

    @NonNull
    private String name;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "course" , fetch = FetchType.EAGER)
    private List<Enrollment> enrollments;
}
