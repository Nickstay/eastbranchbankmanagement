package org.bankmanagement.data_transfer_objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorResponse {
    private String code;
    private int status;
    private String message;
}
