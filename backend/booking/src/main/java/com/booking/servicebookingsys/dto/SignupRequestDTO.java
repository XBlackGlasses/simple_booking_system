package com.booking.servicebookingsys.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data

public class SignupRequestDTO {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String lastName;

    private String phone;

}
