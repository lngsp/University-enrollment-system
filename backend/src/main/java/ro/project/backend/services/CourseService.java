package ro.project.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.project.backend.entities.Course;
import ro.project.backend.exceptions.CourseAlreadyExistsException;
import ro.project.backend.exceptions.CourseNotFoundException;
import ro.project.backend.repositories.db1.CourseDb1Repository;
import ro.project.backend.repositories.db2.CourseDb2Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CourseService {
    @Autowired
    private CourseDb1Repository courseDb1Repository;

    @Autowired
    private CourseDb2Repository courseDb2Repository;

    public List<Course> getAllCoursesFromDatabase1(){
        List<Course> listDb1 = courseDb1Repository.findAll();
        if(listDb1.isEmpty()==true){
            throw new CourseNotFoundException("There are no courses in the first databases!");
        }
        return listDb1;
    }

    public List<Course> getAllCoursesFromDatabase2(){
        List<Course> listDb2 = courseDb2Repository.findAll();
        if(listDb2.isEmpty()==true){
            throw new CourseNotFoundException("There are no courses in the second databases!");
        }
        return listDb2;
    }

    public Course getCoursesByCode(String code){
        Course c1 = courseDb1Repository.findByCode(code);
        Course c2 = courseDb2Repository.findByCode(code);
        if(c1==null && c2==null)
            throw new CourseNotFoundException("There is no course with code " + code + " in database!");
        else
            // because I insert/remove a course into/from both databases,
            // I consider that if I have found one, then it exists in both databases
            return c1;
    }

    public void addCourse(Course course){

        if (courseDb1Repository.findByCode(course.getCode()) != null ||
                courseDb2Repository.findByCode(course.getCode()) != null) {
            throw new CourseAlreadyExistsException("A course with the same code already exists.");
        }
        courseDb1Repository.save(course);
        courseDb2Repository.save(course);
    }

    public void deleteCourse(String code){
        Course c_db1 = courseDb1Repository.findByCode(code);
        Course c_db2 = courseDb2Repository.findByCode(code);
        if(c_db1 == null && c_db2 == null){
            throw new CourseNotFoundException("There is no course with code " + code + " in the database!");
        }
        else {
            courseDb1Repository.deleteByCode(code);
            courseDb2Repository.deleteByCode(code);
        }
    }
}
