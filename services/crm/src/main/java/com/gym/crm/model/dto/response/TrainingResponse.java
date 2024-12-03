package com.gym.crm.model.dto.response;

import java.time.Duration;
import java.time.LocalDate;

public record TrainingResponse(
        Long trainingId,
        Long traineeId,
        Long trainerId,
        String trainingName,
        String trainingTypeName,
        LocalDate trainingDate,
        Duration duration
) {}
