package ro.project.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.project.backend.entities.Student;
import ro.project.backend.services.StudentService;
import ro.project.backend.exceptions.StudentAlreadyExistsException;
import ro.project.backend.exceptions.StudentNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/backend/students")
@RequiredArgsConstructor
public class StudentController {

    @Autowired
    private StudentService studentService;

    //////API

    /////////////////////////////////////////////////////////////
    ////////////////             GET             ////////////////
    /////////////////////////////////////////////////////////////
    @GetMapping("/")
    public ResponseEntity<?> getAllStudents(){
        //get all students from db1 and db2
        try{
            List<Student> list = studentService.getAllStudents();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    @GetMapping("/db1")
    public ResponseEntity<?> getAllStudentsFromDatabase1(){
        //get all student from db1
        try{
            List<Student> list = studentService.getAllStudentsFromDatabase1();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    @GetMapping("/db2")
    public ResponseEntity<?> getAllStudentsFromDatabase2(){
        //get all student from db2
        try{
            List<Student> list = studentService.getAllStudentsFromDatabase2();
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        }catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /////////////////////////////////////////////////////////////
    ////////////////             POST             ///////////////
    /////////////////////////////////////////////////////////////
    @PostMapping("/")
    public ResponseEntity<?> createStudent(@RequestBody Student student){
        //add a student
        try{
            studentService.addStudent(student);
            return new ResponseEntity<>("The student was successfully added!", new HttpHeaders(), HttpStatus.OK);
        }catch (StudentAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
        }
    }

    /////////////////////////////////////////////////////////////////
    ////////////////             DELETE             ////////////////
    ////////////////////////////////////////////////////////////////
    @DeleteMapping("/")
    public ResponseEntity<?> deleteStudent(@RequestParam("name") String name){
        try{
            studentService.deleteStudent(name);
            return new ResponseEntity<>("The student was successfully deleted!", new HttpHeaders(), HttpStatus.OK);
        }
        catch (StudentNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }
}
