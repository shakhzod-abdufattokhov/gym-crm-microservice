package com.gym.crm.service.impl;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.model.entity.User;
import com.gym.crm.repository.UserRepository;
import com.gym.crm.service.JwtService;
import com.gym.crm.service.TraineeService;
import com.gym.crm.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register() {
        TraineeRequest request = new TraineeRequest(
                "Jim",
                "Rohn",
                LocalDate.of(2000, 1, 1),
                "USA",
                true
        );

        RegistrationResponse registrationResponse = new RegistrationResponse("Jim.Rohn", "qwerty");

        when(traineeService.create(request)).thenReturn(registrationResponse);

        RegistrationResponse response = traineeService.create(request);

        String username = request.firstName().concat(".").concat(request.lastName());
        assertEquals(username, response.username());

        verify(traineeService, times(1)).create(any());
        verifyNoMoreInteractions(traineeService);
    }

    @Test
    void testRegister() {
        TrainerRequest request = new TrainerRequest(
                "Jim",
                "Rohn",
                1L,
                true
        );

        RegistrationResponse registrationResponse = new RegistrationResponse("Jim.Rohn", "qwerty");

        when(trainerService.create(request)).thenReturn(registrationResponse);

        RegistrationResponse response = trainerService.create(request);

        String username = request.firstName().concat(".").concat(request.lastName());
        assertEquals(username, response.username());

        verify(trainerService, times(1)).create(any());
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void login() {
        UserLoginRequest request = new UserLoginRequest(
                "Jim.Rohn",
                "qwerty"
        );

        User user = User.builder()
                        .username(request.username())
                        .password(request.password())
                        .build();

        String jwt = "token";

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwt);

        String token = authService.login(request);

        assertEquals(jwt, token);

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(jwtService, times(1)).generateToken(user);
        verifyNoMoreInteractions(userRepository, jwtService);
    }
}