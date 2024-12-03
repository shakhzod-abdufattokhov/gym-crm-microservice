package com.gym.crm.mapper;

import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.response.TraineeResponse;
import com.gym.crm.model.entity.Trainee;
import com.gym.crm.model.entity.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeMapperTest {

    @InjectMocks
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Test
    void toTrainee() {
        TraineeRequest traineeRequest = new TraineeRequest(
                "Jim",
                "Rohn",
                LocalDate.now(),
                "USA",
                true
        );

        Trainee trainee = traineeMapper.toTrainee(traineeRequest);

        assertEquals(traineeRequest.firstName(), trainee.getFirstName());
        assertEquals(traineeRequest.lastName(), trainee.getLastName());
        assertEquals(traineeRequest.dateOfBirth(), trainee.getDateOfBirth());
        assertEquals(traineeRequest.address(), trainee.getAddress());
        assertEquals(traineeRequest.isActive(), trainee.getIsActive());
    }

    @Test
    void toTraineeResponse() {
        List<Trainer> trainers = List.of(
                new Trainer(),
                new Trainer()
        );
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("Iman")
                .lastName("Gadzhi")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .trainers(trainers)
                .build();

        when(trainerMapper.toTrainerResponses(trainers)).thenReturn(List.of());

        TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(trainee);
        assertEquals(trainee.getId(), traineeResponse.userId());
        assertEquals(trainee.getFirstName(), traineeResponse.firstName());
        assertEquals(trainee.getLastName(), traineeResponse.lastName());
        assertEquals(trainee.getDateOfBirth(), traineeResponse.dateOfBirth());
        assertEquals(trainee.getAddress(), traineeResponse.address());
        assertEquals(trainee.getIsActive(), traineeResponse.isActive());
        assertEquals(trainee.getTrainers().size(), trainers.size());
    }

    @Test
    void toTraineeResponses() {
        Trainee trainee1 = Trainee.builder()
                .id(1L)
                .firstName("Iman")
                .lastName("Gadzhi")
                .username("Iman.Gadzhi")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .build();

        Trainee trainee2 = Trainee.builder()
                .id(2L)
                .firstName("Jim")
                .lastName("Rohn")
                .username("Jim.Rohn")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .build();

        List<Trainee> trainees = List.of(trainee1, trainee2);

        List<TraineeResponse> traineeResponses = traineeMapper.toTraineeResponses(trainees);

        assertEquals(trainees.size(), traineeResponses.size());
    }

    @Test
    void toUpdatedTrainee() {
        Trainee trainee = new Trainee(
                "Jim",
                "Rohn",
                true,
                LocalDate.now(),
                "USA"
        );
        TraineeRequest traineeRequest = new TraineeRequest(
                "Iman",
                "Gadzhi",
                LocalDate.now(),
                "USA",
                false
        );

        Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, traineeRequest);
        assertEquals(traineeRequest.firstName(), updatedTrainee.getFirstName());
        assertEquals(traineeRequest.lastName(), updatedTrainee.getLastName());
        assertEquals(traineeRequest.dateOfBirth(), updatedTrainee.getDateOfBirth());
        assertEquals(traineeRequest.address(), updatedTrainee.getAddress());
    }
}