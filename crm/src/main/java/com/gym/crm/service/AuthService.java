package com.gym.crm.service;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.request.UserLoginRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;

import java.io.IOException;

public interface AuthService {

    RegistrationResponse registerTrainee(TraineeRequest registerDto) throws IOException;

    RegistrationResponse registerTrainer(TrainerRequest registerDto) throws IOException;

    String login(UserLoginRequest loginDto);
}
