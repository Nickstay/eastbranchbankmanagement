package org.bankmanagement.exceptions;

public class UserIsDisabledException extends RuntimeException {
    public UserIsDisabledException(String username) {
        super("User is disabled by username: " + username);
    }
}
