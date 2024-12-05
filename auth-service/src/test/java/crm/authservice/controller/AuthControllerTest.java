package crm.authservice.controller;

import crm.authservice.model.dto.LoginRequest;
import crm.authservice.model.dto.RegisterRequest;
import crm.authservice.model.dto.RegistrationResponse;
import crm.authservice.model.dto.TokenValidationRequest;
import crm.authservice.service.AuthService;
import crm.authservice.service.security.CustomUserDetailsService;
import crm.authservice.service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("newUser", "password");
        RegistrationResponse registrationResponse = new RegistrationResponse("newUser", "password");

        when(authService.register(eq(registerRequest))).thenReturn(registrationResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"newUser\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.password").value("password"));

        verify(authService, times(1)).register(eq(registerRequest));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("newUser", "password");
        String token = "jwt-token";

        when(authService.login(eq(loginRequest))).thenReturn(token);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"newUser\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        verify(authService, times(1)).login(eq(loginRequest));
    }

    @Test
    void testValidateToken() throws Exception {
        String token = "valid-token";
        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest(token);
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtService.extractUsername(token)).thenReturn("newUser");
        when(customUserDetailsService.loadUserByUsername("newUser")).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        mockMvc.perform(post("/auth/validate")
                        .contentType("application/json")
                        .content("{\"token\":\"valid-token\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(jwtService, times(1)).extractUsername(token);
        verify(customUserDetailsService, times(1)).loadUserByUsername("newUser");
        verify(jwtService, times(1)).isTokenValid(token, userDetails);
    }

}
