package com.booking.servicebookingsys.controller;

import com.booking.servicebookingsys.dto.AuthenticationRequest;
import com.booking.servicebookingsys.dto.SignupRequestDTO;
import com.booking.servicebookingsys.dto.UserDto;
import com.booking.servicebookingsys.entity.User;
import com.booking.servicebookingsys.repository.UserRepository;
import com.booking.servicebookingsys.services.authentication.AuthService;
import com.booking.servicebookingsys.services.authentication.AuthServiceImpl;
import com.booking.servicebookingsys.services.jwt.UserDetailsServiceImpl;
import com.booking.servicebookingsys.utill.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    // test connection
    @GetMapping("/hello")
    public String hello(){
        return "test api connection successfully";
    }

    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO){
        if(authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Client already exist with this email!", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.signupClient(signupRequestDTO);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO){
        if(authService.presentByEmail(signupRequestDTO.getEmail())){
            return new ResponseEntity<>("Company already exist with this email!", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.signupCompany(signupRequestDTO);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    // JWT認證， generate token to login client
    @Autowired
    private AuthenticationManager authenticationManager;

    public static final String TOKEN_PREFIX = "Bearer ";    // In the Authorization header.
    public static final String HEADER_STRING = "Authorization";     // the header carry token.
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping({"/authenticate"})
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {

        try{
            // authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()
            ));


        }catch(BadCredentialsException e){    // catch bad credentials exception

             throw  new BadCredentialsException("Incorrect username or password", e);
        }catch (Exception e){
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }


        // generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        // to get user details, need user ID and user role with the JWT
        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

        response.getWriter().write(new JSONObject()
                .put("userId", user.getId())
                .put("role", user.getRole())
                .toString()
        );
        //
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization," +
                "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }

}
