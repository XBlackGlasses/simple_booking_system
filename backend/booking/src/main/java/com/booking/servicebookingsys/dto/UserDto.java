package com.booking.servicebookingsys.dto;

import com.booking.servicebookingsys.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String lastName;

    private String phone;

    private UserRole role;

}
