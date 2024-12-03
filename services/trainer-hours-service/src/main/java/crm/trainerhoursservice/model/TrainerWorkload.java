package crm.trainerhoursservice.model;

import crm.trainerhoursservice.model.constant.ActionType;

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
