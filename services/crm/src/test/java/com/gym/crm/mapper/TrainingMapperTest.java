package com.gym.crm.mapper;

import com.gym.crm.model.dto.request.TrainingRequest;
import com.gym.crm.model.dto.response.TrainingResponse;
import com.gym.crm.model.entity.Trainee;
import com.gym.crm.model.entity.Trainer;
import com.gym.crm.model.entity.Training;
import com.gym.crm.model.entity.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainingMapperTest {

    @InjectMocks
    private TrainingMapper trainingMapper;

    @Test
    void toEntity() {
        TrainingRequest request = new TrainingRequest(
                1L,
                1L,
                "Strength Training",
                1L,
                LocalDate.of(2024, 11, 11),
                Duration.ZERO
        );

        Training training = trainingMapper.toEntity(request);

        assertEquals(request.trainingName(), training.getTrainingName());
        assertEquals(request.trainingDate(), training.getTrainingDate());
        assertEquals(request.duration(), training.getDuration());
    }

    @Test
    void toResponse() {
        Training training = new Training();
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        TrainingType trainingType = new TrainingType();
        trainingType.setName("GYM");

        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        training.setId(1L);
        training.setTrainingName("Strength Training");
        training.setTrainingDate(LocalDate.of(2024, 11, 11));
        training.setDuration(Duration.ZERO);

        TrainingResponse response = trainingMapper.toResponse(training);

        assertEquals(training.getTrainingName(), response.trainingName());
        assertEquals(training.getTrainingDate(), response.trainingDate());
        assertEquals(training.getDuration(), response.duration());
    }

    @Test
    void toResponses() {
        List<Training> trainings = List.of(
                new Training(1L, new Trainee(), new Trainer(),
                        "Strength Training", new TrainingType(), LocalDate.of(2023, 8, 28), Duration.ZERO),

                new Training(2L, new Trainee(), new Trainer(),
                        "Strength Training", new TrainingType(), LocalDate.of(2023, 8, 28), Duration.ZERO)
        );

        List<TrainingResponse> responses = trainingMapper.toResponses(trainings);

        assertEquals(trainings.size(), responses.size());
        for (int i = 0; i < trainings.size(); i++) {
            Training training = trainings.get(i);
            TrainingResponse response = responses.get(i);

            assertEquals(training.getId(), response.trainingId());
            assertEquals(training.getTrainingName(), response.trainingName());
            assertEquals(training.getTrainingDate(), response.trainingDate());
            assertEquals(training.getDuration(), response.duration());
        }
    }


}