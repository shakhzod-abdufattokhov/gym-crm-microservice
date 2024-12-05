package com.gym.crm.model.dto.response;

import java.time.LocalDate;
import java.util.List;

public record TraineeResponse(
        Long userId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerResponse> trainers
) {}
