package crm.authservice.service;

import crm.authservice.model.dto.LoginRequest;
import crm.authservice.model.dto.RegisterRequest;
import crm.authservice.model.dto.RegistrationResponse;
import crm.authservice.model.entity.AuthUser;
import crm.authservice.repository.UserRepository;
import crm.authservice.service.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_UserDoesNotExist() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("newUser", "password");

        when(userRepository.existsAuthUserByUsername(registerRequest.username())).thenReturn(false);

        RegistrationResponse response = authService.register(registerRequest);

        assertEquals(registerRequest.username(), response.username());
        assertEquals(registerRequest.password(), response.password());

        verify(userRepository).existsAuthUserByUsername(registerRequest.username());
        verify(userRepository).save(argThat(authUser ->
                authUser.getUsername().equals(registerRequest.username()) &&
                        authUser.getPassword().equals(registerRequest.password())
        ));
    }


    @Test
    void testRegister_UserAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest("existingUser", "password");

        when(userRepository.existsAuthUserByUsername(registerRequest.username())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals("User exists with the username: existingUser", exception.getMessage());
        verify(userRepository).existsAuthUserByUsername(registerRequest.username());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_ValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        AuthUser user = AuthUser.builder()
                .username(loginRequest.username())
                .password(loginRequest.password())
                .build();
        String token = "mockJwtToken";

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);

        String resultToken = authService.login(loginRequest);

        assertEquals(token, resultToken);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));
        verify(userRepository).findByUsername(loginRequest.username());
        verify(jwtService).generateToken(user);
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest("nonExistentUser", "password");

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("User with username : nonExistentUser not found", exception.getMessage());
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));
        verify(userRepository).findByUsername(loginRequest.username());
        verify(jwtService, never()).generateToken(any());
    }
}
