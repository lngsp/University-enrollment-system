package ro.project.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NonNull
    private Date dateEnrolled;

    private Integer finalGrade;

    @Builder.Default
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
