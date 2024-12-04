package com.gym.crm.controller;

import com.gym.crm.model.dto.request.TrainingRequest;
import com.gym.crm.model.dto.response.ApiResponse;
import com.gym.crm.model.dto.response.TrainingResponse;
import com.gym.crm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        TrainingRequest request = new TrainingRequest(1L, 1L, "Bodybuilding", 1L, LocalDate.now(), Duration.ofHours(2));
        ApiResponse<Void> apiResponse = new ApiResponse<>(200, "Training created successfully!", true);

        ApiResponse<Void> response = trainingController.create(request);

        assertEquals(200, response.statusCode());
        assertEquals("Training created successfully!", response.message());

        verify(trainingService, times(1)).create(any(TrainingRequest.class));
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    void findById() {
        Long id = 1l;
        TrainingResponse trainingResponse = new TrainingResponse(1L, 1L, 1L, "Bodybuilding", "GYM", LocalDate.now(), Duration.ofHours(2));

        when(trainingService.findById(id)).thenReturn(trainingResponse);
        ApiResponse<TrainingResponse> response = trainingController.findById(id);

        assertEquals(200, response.statusCode());
        assertEquals("Successfully found", response.message());
        assertEquals(trainingResponse, response.data());

        verify(trainingService, times(1)).findById(id);
        verifyNoMoreInteractions(trainingService);
    }

    @Test
    void findAll() {
        List<TrainingResponse> trainings = List.of(
                new TrainingResponse(1L, 1L, 1L, "Bodybuilding", "Gym", LocalDate.now(), Duration.ofHours(2)),
                new TrainingResponse(1L, 1L, 1L, "Bodybuilding", "GYM", LocalDate.now(), Duration.ofHours(1))
        );

        when(trainingService.findAll()).thenReturn(trainings);

        ApiResponse<List<TrainingResponse>> response = trainingController.findAll();

        assertEquals(200, response.statusCode());
        assertEquals("Success!", response.message());
        assertEquals(trainings, response.data());

        verify(trainingService, times(1)).findAll();
        verifyNoMoreInteractions(trainingService);
    }
}