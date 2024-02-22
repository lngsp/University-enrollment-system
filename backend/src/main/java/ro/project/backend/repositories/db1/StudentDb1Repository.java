package ro.project.backend.repositories.db1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.project.backend.entities.Course;
import ro.project.backend.entities.Student;

@Repository
@Qualifier("studentDb1Repository")
public interface StudentDb1Repository extends JpaRepository<Student, Integer> {
    Student findByNumber(String number);

    Student findByName(String name);

    void deleteByNumber(String number);
}
