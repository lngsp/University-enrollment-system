package ro.project.backend.repositories.db2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.project.backend.entities.Course;

@Repository
@Qualifier("courseDb2Repository")
public interface CourseDb2Repository extends JpaRepository<Course, Integer> {
    Course findByCode(String code);

    void deleteByCode(String code);
}