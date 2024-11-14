package com.booking.servicebookingsys.services.authentication;

import com.booking.servicebookingsys.dto.SignupRequestDTO;
import com.booking.servicebookingsys.dto.UserDto;

public interface AuthService {

    UserDto signupClient(SignupRequestDTO signupRequestDTO);

    Boolean presentByEmail(String email);

    UserDto signupCompany(SignupRequestDTO signupRequestDTO);
}
