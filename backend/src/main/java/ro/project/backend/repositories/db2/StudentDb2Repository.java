package ro.project.backend.repositories.db2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.project.backend.entities.Student;

@Repository
@Qualifier("studentDb2Repository")
public interface StudentDb2Repository extends JpaRepository<Student, Integer> {
    Student findByNumber(String number);

    Student findByName(String name);

    void deleteByNumber(String number);
}
