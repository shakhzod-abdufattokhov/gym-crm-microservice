package com.gym.crm.service.impl;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.service.TraineeService;
import com.gym.crm.service.TrainerService;
import com.gym.crm.service.client.AuthClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerTrainee() throws Exception {
        TraineeRequest request = new TraineeRequest(
                "Jim",
                "Rohn",
                LocalDate.of(2022, 11, 21),
                "USA",
                true
        );

        RegistrationResponse expectedResponse = new RegistrationResponse("Jim.Rohn", "qwerty");

        when(traineeService.create(request)).thenReturn(expectedResponse);

        RegistrationResponse actualResponse = authService.registerTrainee(request);

        assertEquals(expectedResponse, actualResponse);
        verify(traineeService, times(1)).create(request);
    }

    @Test
    void registerTrainer() throws Exception {
        TrainerRequest request = new TrainerRequest(
                "Jim",
                "Rohn",
                1L,
                true
        );

        RegistrationResponse expectedResponse = new RegistrationResponse("Jim.Rohn", "qwerty");

        when(trainerService.create(request)).thenReturn(expectedResponse);

        RegistrationResponse actualResponse = authService.registerTrainer(request);

        assertEquals(expectedResponse, actualResponse);
        verify(trainerService, times(1)).create(request);
    }

    @Test
    void login() {
        UserLoginRequest loginRequest = new UserLoginRequest("Jim.Rohn", "qwerty");
        String token = "mockToken";

        when(authClient.login(loginRequest)).thenReturn(ResponseEntity.ok(token));

        String actualToken = authService.login(loginRequest);

        assertEquals(token, actualToken);
        verify(authClient, times(1)).login(loginRequest);
    }
}
