package ro.project.backend.exceptions;

public class StudentNotFoundException  extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
