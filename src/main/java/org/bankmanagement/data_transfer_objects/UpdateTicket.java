package org.bankmanagement.data_transfer_objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateTicket {
    @Email
    private String email;
    @Size(min = 3, max = 50)
    private String username;
    @Size(min = 3, max = 50)
    private String password;

}
