package com.gym.crm.model.dto.response;

import java.util.List;

public record TrainerResponse(
        Long id,
        String firstName,
        String lastName,
        String specialization,
        boolean isActive,
        List<TraineeResponse> traineeResponses
) {}
