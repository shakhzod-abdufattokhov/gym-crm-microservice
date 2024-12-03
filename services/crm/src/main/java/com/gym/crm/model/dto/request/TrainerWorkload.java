package com.gym.crm.model.dto.request;

import com.gym.crm.model.dto.enums.ActionType;

import java.time.LocalDate;

public record TrainerWorkload(
        String username,
        String firstName,
        String lastName,
        boolean isActive,
        LocalDate trainingDate,
        double trainingDuration,
        ActionType actionType
){}
