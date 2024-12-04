package com.gym.crm.service;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.model.dto.response.TraineeResponse;
import com.gym.crm.model.dto.response.TrainerResponse;
import com.gym.crm.model.entity.Trainee;

import java.util.List;

public interface TraineeService {

    RegistrationResponse create(TraineeRequest trainee);

    TraineeResponse update(String username, TraineeRequest trainee);

    void delete(String username);

    TraineeResponse findByUsername(String username);

    List<TraineeResponse> findAll();

    void deleteAll();

    void updatePassword(String username, String oldPassword, String newPassword);

    void deActivateUser(String username);

    void activateUser(String username);

    List<TrainerResponse> findAllUnassignedTrainers(String username);

    boolean isDBEmpty();

    Trainee one(String traineeUsername);
}
