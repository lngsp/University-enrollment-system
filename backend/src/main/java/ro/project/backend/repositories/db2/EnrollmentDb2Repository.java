package ro.project.backend.repositories.db2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.project.backend.entities.Course;
import ro.project.backend.entities.Enrollment;
import ro.project.backend.entities.Student;

import java.util.List;

@Repository
@Qualifier("enrollmentDb2Repository")
public interface EnrollmentDb2Repository extends JpaRepository<Enrollment, Integer> {
    @Override
    List<Enrollment> findAll();

    @Query(value = "SELECT course_id \n" +
            "FROM enrollment e \n" +
            "WHERE e.student_id = :id\n", nativeQuery = true)
    List<String> findCoursesByStudentId(@Param("id") Integer id);

    @Query(value = "SELECT s.name \n" +
            "FROM enrollment e, student s \n" +
            "WHERE e.course_id = :code AND e.student_id = s.id \n", nativeQuery = true)
    List<String> findStudentsByCourseCode(@Param("code") String code);

    @Query(value = "SELECT * \n" +
            "FROM enrollment e \n" +
            "WHERE e.course_id = :code AND e.student_id = :id\n", nativeQuery = true)
    Enrollment findByCourseCodeAndStudentId(@Param("code") String code, @Param("id") Integer id);
}
