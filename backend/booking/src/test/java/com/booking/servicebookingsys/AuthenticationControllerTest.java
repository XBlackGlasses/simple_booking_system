package com.booking.servicebookingsys;


import com.booking.servicebookingsys.dto.SignupRequestDTO;
import com.booking.servicebookingsys.dto.UserDto;
import com.booking.servicebookingsys.entity.User;
import com.booking.servicebookingsys.repository.UserRepository;
import com.booking.servicebookingsys.services.authentication.AuthService;
import com.booking.servicebookingsys.utill.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import com.booking.servicebookingsys.services.jwt.UserDetailsServiceImpl;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;


    @Test
    @Transactional
    public void testSignup() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@email.com");
        userDto.setPassword("test");
        // 透過mockito模擬呼叫authService，直接指定回傳值。 authService 要用@MockBean註解
        Mockito.when(authService.signupClient(Mockito.any(SignupRequestDTO.class))).thenReturn(userDto);

         /*
        創建一個requestBuilder，
        requestBuilder他會決定要發起的http requst，url路徑
        甚至header，他其實就是一個APItester的概念
         */

        //RequestBuilder也使用了Builder設計模式
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/client/sign-up")    // post api
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@email.com\",\"password\":\"test\"}");


        /*
        mockMvc.perform()程式，
        他的用途就是在執行上面的requestBuilder
        這個方法就等同於在APItester按下send
         */
        mockMvc.perform(requestBuilder)
                /*
                perform後面的程式，
                andExpect這個方法就是用來驗證結果，
                很像assert的概念，主要有
                andDo()、andExpect、andReturn
                來輸出、驗證、取得結果
                 */
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(userDto.getEmail())));

    }

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testAuthentication() throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                new ArrayList<>());

        Authentication authentication = Mockito.mock(Authentication.class);
        authentication.setAuthenticated(true);

        // Mock situations
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authentication);

        Mockito.when(userDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);

        Mockito.when(jwtUtil.generateToken(userDetails.getUsername())).thenReturn("123456");    // mock token.

        Mockito.when(userRepository.findFirstByEmail("test@gmail.com")).thenReturn(user);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/authenticate")    // post api
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test@gmail.com\",\"password\":\"test\"}");

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
                ;
    }

}
