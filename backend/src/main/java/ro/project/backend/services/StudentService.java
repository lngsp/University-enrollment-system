package ro.project.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.project.backend.entities.Student;
import ro.project.backend.exceptions.StudentAlreadyExistsException;
import ro.project.backend.exceptions.StudentNotFoundException;
import ro.project.backend.repositories.db1.StudentDb1Repository;
import ro.project.backend.repositories.db2.StudentDb2Repository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentService {

    @Autowired
    private StudentDb1Repository studentDb1Repository;

    @Autowired
    private StudentDb2Repository studentDb2Repository;

    public List<Student> getAllStudents(){
        List<Student> listDb1 = studentDb1Repository.findAll();
        List<Student> listDb2 = studentDb2Repository.findAll();
        if(listDb1.isEmpty()==true && listDb2.isEmpty()==true){
            throw new StudentNotFoundException("There are no students in the databases!");
        }

        if(listDb1.isEmpty()==true)
            //return only the students from the second database and evoid StackOverflowError: null
            return listDb2;
        else if (listDb2.isEmpty()==true)
            //return only the students from the first database and evoid StackOverflowError: null
            return listDb1;
        else{
            // Combine the two lists
            List<Student> combinedList = new ArrayList<>(listDb1);
            combinedList.addAll(listDb2);
            return combinedList;
        }
    }

    public List<Student> getAllStudentsFromDatabase1(){
        List<Student> list = studentDb1Repository.findAll();
        if(list.isEmpty()==true){
            throw new StudentNotFoundException("There are no students in the first database!");
        }
        return list;
    }

    public List<Student> getAllStudentsFromDatabase2(){
        List<Student> list = studentDb2Repository.findAll();
        if(list.isEmpty()==true){
            throw new StudentNotFoundException("There are no students in the second database!");
        }
        return list;
    }

    public Student getStudentByNumber(String number){
        Student s1 = studentDb1Repository.findByNumber(number);
        Student s2 = studentDb2Repository.findByNumber(number);
        if(s1 == null && s2 == null)
            throw new StudentNotFoundException("There is no student with number " + number + " in database!");
        else if(s1 != null)
            return s1;
        else return s2;
    }

    public Student getStudentByName(String name){
        //I considered that there couldn't be 2 students with the same name
        Student s = new Student();
        char firstLetter = name.toUpperCase().charAt(0);

        if (firstLetter >= 'A' && firstLetter <= 'M') {
            s = studentDb1Repository.findByName(name);
        } else if (firstLetter >= 'N' && firstLetter <= 'Z') {
            s = studentDb2Repository.findByName(name);
        }

        if(s == null)
            throw new StudentNotFoundException("There is no student with name: " + name + " in database!");
        return s;
    }

    public void addStudent(Student student){
        if (studentDb1Repository.findByName(student.getName()) != null ||
                studentDb2Repository.findByName(student.getName()) != null) {
            throw new StudentAlreadyExistsException("A student with the same name already exists.");
        }

        if (studentDb1Repository.findByNumber(student.getNumber()) != null ||
                studentDb2Repository.findByNumber(student.getNumber()) != null) {
            throw new StudentAlreadyExistsException("A student with the same number already exists.");
        }

        char firstLetter = student.getName().toUpperCase().charAt(0);

        if (firstLetter >= 'A' && firstLetter <= 'M') {
            studentDb1Repository.save(student);
        } else if (firstLetter >= 'N' && firstLetter <= 'Z') {
            studentDb2Repository.save(student);
        }
    }

    public void deleteStudent(String name){
        Student s = new Student();
        char firstLetter = name.toUpperCase().charAt(0);

        //check if the user exist
        if (firstLetter >= 'A' && firstLetter <= 'M')
            s = studentDb1Repository.findByName(name);
         else if (firstLetter >= 'N' && firstLetter <= 'Z')
            s = studentDb2Repository.findByName(name);

        if(s == null)
            throw new StudentNotFoundException("There is no student with name: " + name + " in the database!");

        //delete the user
        if (firstLetter >= 'A' && firstLetter <= 'M')
            studentDb1Repository.deleteById(s.getId());
        else if (firstLetter >= 'N' && firstLetter <= 'Z')
            studentDb2Repository.deleteById(s.getId());

    }
}
