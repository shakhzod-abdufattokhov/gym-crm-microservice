package com.gym.crm.controller;

import com.gym.crm.model.dto.request.TrainingTypeRequest;
import com.gym.crm.model.dto.response.ApiResponse;
import com.gym.crm.model.entity.TrainingType;
import com.gym.crm.service.TrainingTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("/api/v1/training-type")
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody TrainingTypeRequest request) {
        log.info("Creating training type: {}", request);
        trainingTypeService.createTrainingType(request);
        log.info("Created training type: {}", request);
        return new ApiResponse<>(200, true, null, "Training type created successfully!");
    }

    @GetMapping("/name/{name}")
    public ApiResponse<TrainingType> findByName(@PathVariable String name) {
        log.info("Finding training type with name: {}", name);
        TrainingType trainingType = trainingTypeService.findByName(name);
        return new ApiResponse<>(200, true, trainingType, "Successfully found training type");
    }

    @GetMapping("/is-empty")
    public ApiResponse<Boolean> isDatabaseEmpty() {
        log.info("Checking if the training type database is empty");
        boolean isEmpty = trainingTypeService.isDBEmpty();
        return new ApiResponse<>(200, true, isEmpty, isEmpty ? "Database is empty" : "Database " +
                                                                                                        "is not empty");
    }
}

