package ro.project.backend.exceptions;

public class EnrollmentAlreadyExistsException extends RuntimeException {
    public EnrollmentAlreadyExistsException(String message) {
        super(message);
    }
}