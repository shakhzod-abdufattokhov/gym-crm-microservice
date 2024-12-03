package com.gym.crm.service;

import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.model.dto.response.TrainerResponse;
import com.gym.crm.model.entity.Trainer;

import java.util.List;

public interface TrainerService {

    RegistrationResponse create(TrainerRequest trainer);

    TrainerResponse update(String username, TrainerRequest trainer);

    TrainerResponse findByUsername(String username);

    List<TrainerResponse> findAll();

    void updatePassword(String username, String oldPassword, String newPassword);

    void deActivateUser(String username);

    void activateUser(String username);

    boolean isDBEmpty();

    Trainer one(String trainerUsername);
}
