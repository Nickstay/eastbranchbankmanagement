package org.bankmanagement.data_transfer_objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
public class RegisterTicket {
    @NotNull(message = "Username field must be filled")
    @Size(min = 3, max = 50, message = "Username length shall be between 3 and 50 characters")
    private String username;

    @NotNull(message = "Password field must be filled")
    @Size(min = 4, max = 50, message = "Password length shall be between 4 and 50 characters")
    private String password;

    @NotNull(message = "Email field must be filled")
    @Email(message = "Email shall match pattern: \"chosenname@domain.org\"")
    private String email;
}
