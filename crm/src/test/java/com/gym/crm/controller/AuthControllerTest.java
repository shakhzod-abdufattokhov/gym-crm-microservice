package com.gym.crm.controller;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import com.gym.crm.model.dto.response.ApiResponse;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTrainee() throws IOException {
        TraineeRequest traineeRequest = new TraineeRequest("Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
        RegistrationResponse registrationResponse = new RegistrationResponse("Iman.Gadzhi", "qwerty");
        when(authService.register(traineeRequest)).thenReturn(registrationResponse);

        ApiResponse<RegistrationResponse> response = authController.register(traineeRequest);

        assertEquals(201, response.statusCode());
        assertEquals("Saved successfully!", response.message());
        verify(authService, times(1)).register(traineeRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void registerTrainer() throws IOException {
        TrainerRequest trainerRequest = new TrainerRequest("John", "Doe", 1L, true);
        RegistrationResponse registrationResponse = new RegistrationResponse("John.Doe", "qwerty");

        when(authService.register(trainerRequest)).thenReturn(registrationResponse);

        ApiResponse<RegistrationResponse> response = authController.register(trainerRequest);

        assertEquals(201, response.statusCode());
        assertEquals("Saved successfully!", response.message());
        verify(authService, times(1)).register(trainerRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void login() {
        UserLoginRequest loginRequest = new UserLoginRequest("Iman.Gadzhi", "password123");
        String token = "jwt-token";

        when(authService.login(loginRequest)).thenReturn(token);

        ApiResponse<String> login = authController.login(loginRequest);

        assertEquals(login.data(), token);
        verify(authService, times(1)).login(loginRequest);
        verifyNoMoreInteractions(authService);
    }

}