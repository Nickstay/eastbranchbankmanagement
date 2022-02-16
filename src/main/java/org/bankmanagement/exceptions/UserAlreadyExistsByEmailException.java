package org.bankmanagement.exceptions;

public class UserAlreadyExistsByEmailException extends RuntimeException {
    public UserAlreadyExistsByEmailException(String email) {
        super("User already exists by email: " + email);
    }
}
