package com.gym.crm.service.impl;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.service.AuthService;
import com.gym.crm.service.TraineeService;
import com.gym.crm.service.TrainerService;
import com.gym.crm.service.client.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthClient authClient;

    @Override
    public String login(UserLoginRequest loginDto) {
        return authClient.login(loginDto).getBody();
    }

    @Override
    public RegistrationResponse register(TraineeRequest registerDto) throws IOException {
        return traineeService.create(registerDto);
    }

    @Override
    public RegistrationResponse register(TrainerRequest registerDto) throws IOException {
        return trainerService.create(registerDto);
    }


}
