package ro.project.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.project.backend.entities.Course;
import ro.project.backend.entities.Enrollment;
import ro.project.backend.entities.Student;
import ro.project.backend.exceptions.CourseNotFoundException;
import ro.project.backend.exceptions.EnrollmentAlreadyExistsException;
import ro.project.backend.exceptions.EnrollmentNotFoundException;
import ro.project.backend.exceptions.StudentNotFoundException;
import ro.project.backend.repositories.db1.EnrollmentDb1Repository;
import ro.project.backend.repositories.db2.EnrollmentDb2Repository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EnrollmentService {
    @Autowired
    private EnrollmentDb1Repository enrollmentDb1Repository;

    @Autowired
    private EnrollmentDb2Repository enrollmentDb2Repository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;


    public List<Enrollment> getAllEnrollmentFromDb1(){
        List<Enrollment> list = enrollmentDb1Repository.findAll();
        if(list.isEmpty()==true){
            throw new EnrollmentNotFoundException("There are no enrollments in the first database!");
        };
        return list;
    }

    public List<Enrollment> getAllEnrollmentFromDb2(){
        List<Enrollment> list = enrollmentDb2Repository.findAll();
        if(list.isEmpty()==true){
            throw new EnrollmentNotFoundException("There are no enrollments in the second database!");
        }
        return list;
    }

    public List<Enrollment> getAllEnrollment(){
        List<Enrollment> list1 = enrollmentDb1Repository.findAll();
        List<Enrollment> list2 = enrollmentDb2Repository.findAll();
        if(list1.isEmpty()==true && list2.isEmpty()==true){
            throw new EnrollmentNotFoundException("There are no enrollments in the databases!");
        }

        if(list1.isEmpty()==true)
            //return only the enrollments from the second database and evoid StackOverflowError: null
            return list2;
        else if (list2.isEmpty()==true)
            //return only the enrollments from the first database and evoid StackOverflowError: null
            return list1;
        else{
            // Combine the two lists
            List<Enrollment> combinedList = new ArrayList<>(list1);
            combinedList.addAll(list2);
            return combinedList;
        }
    }

    public List<String> getCoursesByStudentName(String name){
        try{
            Student s = studentService.getStudentByName(name);
            List<String> courseList = new ArrayList<>();

            //check the student name to see in witch database is stored
            char firstLetter = name.toUpperCase().charAt(0);
            if (firstLetter >= 'A' && firstLetter <= 'M') {
                 courseList = enrollmentDb1Repository.findCoursesByStudentId(s.getId());

            } else if (firstLetter >= 'N' && firstLetter <= 'Z') {
                courseList = enrollmentDb2Repository.findCoursesByStudentId(s.getId());
            }

            if(courseList.isEmpty() == true)
                //there is no enrollment find
                throw new EnrollmentNotFoundException("Student " + name +" is not enrolled in any course!");

            return courseList;
        }catch (StudentNotFoundException e){
            throw new StudentNotFoundException(e.getMessage());
        }
    }

    public List<String> getStudentsByCourseCode(String code){
        try{
            courseService.getCoursesByCode(code);

            List<String> studentNameList1 = enrollmentDb1Repository.findStudentsByCourseCode(code);
            List<String> studentNameList2 = enrollmentDb2Repository.findStudentsByCourseCode(code);

            if(studentNameList1.isEmpty() == true && studentNameList2.isEmpty() == true){
                //no student
                throw new StudentNotFoundException("There are no students at this course!");
            }
            else if(studentNameList1.isEmpty() == true && studentNameList2.isEmpty() == false)
                //there are students from db2
                return studentNameList2;
            else if(studentNameList2.isEmpty() == true && studentNameList1.isEmpty() == false)
                //there are students from db1
                return studentNameList1;
            else{
                //combine the student from db1 and db2
                List<String> combinedList = new ArrayList<>(studentNameList1);
                combinedList.addAll(studentNameList2);
                return combinedList;
            }

        }catch (CourseNotFoundException e){
            throw new CourseNotFoundException(e.getMessage());
        }
    }

    public Enrollment findByCourseAndStudent(String name, String code){
        //get the enrollment for a student based on his name (unique) and the course code
        try{
            Student s = studentService.getStudentByName(name);
            Course c = courseService.getCoursesByCode(code);
            Enrollment e = new Enrollment();

            //check the student name to see in witch database is stored
            char firstLetter = s.getName().toUpperCase().charAt(0);
            if (firstLetter >= 'A' && firstLetter <= 'M') {
                 e = enrollmentDb1Repository.findByCourseCodeAndStudentId(c.getCode(), s.getId());
            } else if (firstLetter >= 'N' && firstLetter <= 'Z') {
                 e = enrollmentDb2Repository.findByCourseCodeAndStudentId(c.getCode(), s.getId());
            }

            return e;
        }catch (StudentNotFoundException e){
            throw new StudentNotFoundException(e.getMessage());
        }
        catch (CourseNotFoundException e){
            throw new CourseNotFoundException(e.getMessage());
        }
    }

    public Map<String, List<String>> getCoursesForStudents(List<Student> students) {
        Map<String, List<String>> result = new HashMap<>();

        for (Student student : students) {
            List<String> courses = getCoursesByStudentName(student.getName());
            result.put(student.getName(), courses);
        }

        return result;
    }

    public void addEnrollemnt(String studentName, String courseCode){
        try{
            Student s = studentService.getStudentByName(studentName);
            Course c = courseService.getCoursesByCode(courseCode);

            if(findByCourseAndStudent(s.getName(), c.getCode()) != null){
                throw new EnrollmentAlreadyExistsException("Student " + s.getName() +" is already enrolled in course " + c.getName());
            }

            Enrollment newEnrollment = new Enrollment();
            newEnrollment.setStudent(s);
            newEnrollment.setCourse(c);
            newEnrollment.setDateEnrolled(new Date());

            char firstLetter = s.getName().toUpperCase().charAt(0);

            if (firstLetter >= 'A' && firstLetter <= 'M') {
                enrollmentDb1Repository.save(newEnrollment);
            } else if (firstLetter >= 'N' && firstLetter <= 'Z') {
                enrollmentDb2Repository.save(newEnrollment);
            }
        }
        catch (StudentNotFoundException e){
            throw new StudentNotFoundException(e.getMessage());
        }
        catch (CourseNotFoundException e){
            throw new CourseNotFoundException(e.getMessage());
        }
    }

    public void changeFinalGrade(Integer grade, String studentName, String courseCode){
        Enrollment enrollment = findByCourseAndStudent(studentName, courseCode);
        if( enrollment == null ){
            throw new EnrollmentNotFoundException("Student " + studentName +" is not enrolled in course " + courseCode);
        }
        enrollment.setFinalGrade(grade);
    }
}
