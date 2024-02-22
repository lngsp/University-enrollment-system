package ro.project.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.project.backend.exceptions.CourseNotFoundException;
import ro.project.backend.services.CourseService;
import ro.project.backend.entities.Course;
import ro.project.backend.exceptions.CourseAlreadyExistsException;
import ro.project.backend.exceptions.StudentNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/backend/courses")
@RequiredArgsConstructor
public class CourseController {
    @Autowired
    private CourseService courseService;

    /////////////////////////////////////////////////////////////
    ////////////////             GET             ////////////////
    /////////////////////////////////////////////////////////////
    @GetMapping("/db1")
    public ResponseEntity<?> getAllCoursesFromDatabase1(){
        try{
            List<Course> list = courseService.getAllCoursesFromDatabase1();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    @GetMapping("/db2")
    public ResponseEntity<?> getAllCoursesFromDatabase2(){
        try{
            List<Course> list = courseService.getAllCoursesFromDatabase2();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.OK);
        }
    }


    //////////////////////////////////////////////////////////////
    ////////////////             POST             ////////////////
    //////////////////////////////////////////////////////////////
    @PostMapping("/")
    public ResponseEntity<?> addCourse(@RequestBody Course course){
        try{
            courseService.addCourse(course);
            return new ResponseEntity<>("The course was successfully added!", new HttpHeaders(), HttpStatus.OK);
        }catch (CourseAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
        }

    }

    ////////////////////////////////////////////////////////////////
    ////////////////             DELETE             ////////////////
    ////////////////////////////////////////////////////////////////
    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCourse(@PathVariable("code") String code){
        try{
            courseService.deleteCourse(code);
            return new ResponseEntity<>("The course was successfully deleted!", new HttpHeaders(), HttpStatus.OK);
        }
        catch (CourseNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }


}
