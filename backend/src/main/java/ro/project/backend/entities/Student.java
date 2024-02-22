package ro.project.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="student",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "number"),
                @UniqueConstraint(columnNames = "name")
        })
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String number; //mobile phone number

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Enrollment> enrollments;

}