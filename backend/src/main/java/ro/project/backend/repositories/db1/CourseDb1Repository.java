package ro.project.backend.repositories.db1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.project.backend.entities.Course;

import java.util.List;

@Repository
@Qualifier("courseDb1Repository")
public interface CourseDb1Repository extends JpaRepository<Course, Integer> {
    Course findByCode(String code);
    void deleteByCode(String code);

}