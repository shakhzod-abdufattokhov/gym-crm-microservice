package com.gym.crm.mapper;

import com.gym.crm.model.dto.request.TrainingRequest;
import com.gym.crm.model.dto.response.TrainingResponse;
import com.gym.crm.model.entity.Training;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingMapper  {

    public Training toEntity(TrainingRequest trainingRequest) {
        Training training = new Training();
        training.setTrainingName(trainingRequest.trainingName());
        training.setTrainingDate(trainingRequest.trainingDate());
        training.setDuration(trainingRequest.duration());
        return training;
    }

    public TrainingResponse toResponse(Training training) {
        return new TrainingResponse(
                training.getId(),
                training.getTrainee().getId(),
                training.getTrainer().getId(),
                training.getTrainingName(),
                training.getTrainingType().getName(),
                training.getTrainingDate(),
                training.getDuration()
        );
    }

    public List<TrainingResponse> toResponses(List<Training> trainings) {
        List<TrainingResponse> trainingResponses = new ArrayList<>();
        for (Training training : trainings) {
            trainingResponses.add(toResponse(training));
        }
        return trainingResponses;
    }
}
