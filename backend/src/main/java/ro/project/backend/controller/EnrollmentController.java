package ro.project.backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.project.backend.entities.Course;
import ro.project.backend.entities.Enrollment;
import ro.project.backend.entities.Student;
import ro.project.backend.exceptions.CourseNotFoundException;
import ro.project.backend.exceptions.EnrollmentNotFoundException;
import ro.project.backend.exceptions.StudentNotFoundException;
import ro.project.backend.services.EnrollmentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/backend/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    /////////////////////////////////////////////////////////////
    ////////////////             GET             ////////////////
    /////////////////////////////////////////////////////////////
    @GetMapping("/")
    public ResponseEntity<?> getAllEnrollment(){
        //enrollments from db1 & db2
        try {
            List<Enrollment> list = enrollmentService.getAllEnrollment();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }
        catch (EnrollmentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/db1")
    public ResponseEntity<?> getAllEnrollmentFromDatabase1(){
        //enrollments from db1 (students between A and M)
        try {
            List<Enrollment> list = enrollmentService.getAllEnrollmentFromDb1();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }
        catch (EnrollmentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/db2")
    public ResponseEntity<?> getAllEnrollmentFromDatabase2(){
        //enrollment from db2 (students between N and Z)
        try {
            List<Enrollment> list = enrollmentService.getAllEnrollmentFromDb2();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }
        catch (EnrollmentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/student")
    public ResponseEntity<?> getCoursesByStudentName(@RequestParam("name") String name){
        ///list all the courses that a student is taking
        try{
            List<String> list = enrollmentService.getCoursesByStudentName(name);
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }catch (EnrollmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/course")
    public ResponseEntity<?> getStudentsByCourseCode(@RequestParam("code") String code){
        /// list all the students taking a certain course
        try{
            List<String> list = enrollmentService.getStudentsByCourseCode(code);
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }catch (EnrollmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/students")
    public ResponseEntity<?> getCoursesForStudents(@RequestBody List<Student> students){
        /// lists all the courses that are enrolled in by a given set of students
        try{
            Map<String, List<String>> list = enrollmentService.getCoursesForStudents(students);
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }catch (EnrollmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    //////////////////////////////////////////////////////////////
    ////////////////             POST             ////////////////
    //////////////////////////////////////////////////////////////
    @PostMapping("/")
    public ResponseEntity<?> addEnrollment(@RequestParam("name") String name,
                                           @RequestParam("code") String code){
        try {
            enrollmentService.addEnrollemnt(name, code);
            return new ResponseEntity<>("The enrollment was successfully added!", new HttpHeaders(), HttpStatus.OK);
        }
        catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        catch (CourseNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        catch (EnrollmentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/student")
    public ResponseEntity<?> addFinalGrade(@RequestParam("grade") Integer grade,
                                           @RequestParam("name") String name,
                                           @RequestParam("code") String code){
        //add a final grade to a student based on his name
        try {
            enrollmentService.changeFinalGrade(grade, name, code);
            return new ResponseEntity<>("The final grade was successfully added!", new HttpHeaders(), HttpStatus.OK);
        }
        catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        catch (CourseNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        catch (EnrollmentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }
}
