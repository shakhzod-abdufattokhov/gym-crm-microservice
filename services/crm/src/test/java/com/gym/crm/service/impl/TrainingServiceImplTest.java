package com.gym.crm.service.impl;

import com.gym.crm.exception.CustomNotFoundException;
import com.gym.crm.mapper.TrainingMapper;
import com.gym.crm.model.dto.request.TrainingRequest;
import com.gym.crm.model.dto.response.TrainingResponse;
import com.gym.crm.model.entity.Trainee;
import com.gym.crm.model.entity.Trainer;
import com.gym.crm.model.entity.Training;
import com.gym.crm.model.entity.TrainingType;
import com.gym.crm.repository.TraineeRepository;
import com.gym.crm.repository.TrainerRepository;
import com.gym.crm.repository.TrainingRepository;
import com.gym.crm.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;


    @Test
    void createTrainingSuccessfully() {
        TrainingRequest trainingRequest = new TrainingRequest(1L, 1L, "", 1L, LocalDate.now(), Duration.ofDays(1L));
        Trainer trainer = new Trainer();
        trainer.setTrainees(new ArrayList<>());
        trainer.setId(1L);

        Trainee trainee = new Trainee();
        trainee.setTrainers(new ArrayList<>());
        trainee.setId(1L);

        TrainingType trainingType = new TrainingType(1L, "GYM");

        Training training = new Training();

        when(trainerRepository.findByIdWithTrainees(1L)).thenReturn(Optional.of(trainer));
        when(traineeRepository.findByIdWithTrainers(1L)).thenReturn(Optional.of(trainee));
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingType));
        when(trainingMapper.toEntity(trainingRequest)).thenReturn(training);

        trainingService.create(trainingRequest);

        verify(trainerRepository, times(1)).findByIdWithTrainees(1L);
        verify(traineeRepository, times(1)).findByIdWithTrainers(1L);
        verify(trainingMapper, times(1)).toEntity(trainingRequest);
        verify(trainingRepository, times(1)).save(training);
        verify(traineeRepository, times(1)).save(trainee);
        verify(trainerRepository, times(1)).save(trainer);
        verifyNoMoreInteractions(trainerRepository, traineeRepository, trainingMapper, trainingRepository);
    }

    @Test
    public void testCreateTraining_TrainerNotFound() {
        Long traineeId = 1L;
        Long trainerId = 2L;

        TrainingRequest trainingRequest = new TrainingRequest(1L, 2L, "", 1L,LocalDate.now(), Duration.ofDays(1L));

        when(trainerRepository.findByIdWithTrainees(trainerId)).thenReturn(Optional.empty());

        CustomNotFoundException e = assertThrows(CustomNotFoundException.class,
                () -> trainingService.create(trainingRequest));

        assertEquals(e.getMessage(), "Trainer not found with id: %d".formatted(trainerId));

        verify(trainerRepository, times(1)).findByIdWithTrainees(trainerId);
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void findByIdFails() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.empty());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            trainingService.findById(1L);
        });
        assertEquals("Training not found with id: %d".formatted(1L), exception.getMessage());

        verify(trainingRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(trainingRepository);
    }

    @Test
    void findById_Success() {
        Long trainingId = 1L;
        Training training = new Training();
        TrainingResponse trainingResponse = new TrainingResponse(
                1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO
        );

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(trainingMapper.toResponse(training)).thenReturn(trainingResponse);

        TrainingResponse response = trainingService.findById(trainingId);

        assertEquals(trainingResponse, response);

        verify(trainingRepository, times(1)).findById(trainingId);
        verify(trainingMapper, times(1)).toResponse(training);
        verifyNoMoreInteractions(trainingRepository, trainingMapper);
    }

    @Test
    void findAllTrainingsFails() {
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> trainingService.findAll());
        assertEquals("Training not found!", exception.getMessage());
        verify(trainingRepository, times(1)).findAll();
        verifyNoMoreInteractions(trainingRepository);
    }

    @Test
    void findAll() {
        List<Training> trainings = List.of(new Training(), new Training());
        List<TrainingResponse> trainingResponses = List.of(
                new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO),
                new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO)
        );

        when(trainingRepository.findAll()).thenReturn(trainings);
        when(trainingMapper.toResponses(trainings)).thenReturn(trainingResponses);

        List<TrainingResponse> response = trainingService.findAll();

        assertEquals(trainingResponses, response);

        verify(trainingRepository, times(1)).findAll();
        verify(trainingMapper, times(1)).toResponses(trainings);
        verifyNoMoreInteractions(trainingRepository, trainingMapper);
    }

    @Test
    public void testFindTraineeTrainings_Success() {
        String username = "trainee1";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String trainerName = "trainer1";
        Long trainingTypeId = 1L;

        List<Training> mockTrainings = List.of(new Training(), new Training());
        TrainingResponse response1 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);
        TrainingResponse response2 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);

        List<TrainingResponse> mockResponses = List.of(response1, response2);

        when(trainingRepository.findAllByCriteria(username, fromDate, toDate, trainerName, trainingTypeId)).thenReturn(mockTrainings);
        when(trainingMapper.toResponses(mockTrainings)).thenReturn(mockResponses);

        List<TrainingResponse> response = trainingService.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingTypeId);

        assertEquals(mockResponses, response);

        verify(trainingRepository, times(1)).findAllByCriteria(username, fromDate, toDate, trainerName, trainingTypeId);
    }

    @Test
    public void testGetTrainingsByTrainer_Success() {
        String username = "trainer1";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String traineeName = "trainee1";

        TrainingResponse response1 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);
        TrainingResponse response2 = new TrainingResponse(1L, 1L, 1L, "", "GYM", LocalDate.now(), Duration.ZERO);

        List<Training> mockTrainings = List.of(new Training(), new Training());
        List<TrainingResponse> mockResponses = List.of(response1, response2);

        when(trainingRepository.findAllByTrainerAndCategory(username, fromDate, toDate, traineeName)).thenReturn(mockTrainings);
        when(trainingMapper.toResponses(mockTrainings)).thenReturn(mockResponses);

        List<TrainingResponse> response = trainingService.getTrainingsByTrainer(username, fromDate, toDate, traineeName);

        assertEquals(mockResponses, response);

        verify(trainingRepository, times(1)).findAllByTrainerAndCategory(username, fromDate, toDate, traineeName);
    }

}