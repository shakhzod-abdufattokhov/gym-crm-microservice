package com.gym.crm.service;

import com.gym.crm.model.dto.request.TrainingTypeRequest;
import com.gym.crm.model.entity.TrainingType;

public interface TrainingTypeService {

    TrainingType findByName(String name);

    void createTrainingType(TrainingTypeRequest trainingTypeRequest);

    boolean isDBEmpty();
}
