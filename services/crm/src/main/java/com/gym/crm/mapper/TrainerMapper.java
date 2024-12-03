package com.gym.crm.mapper;

import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.response.TraineeResponse;
import com.gym.crm.model.dto.response.TrainerResponse;
import com.gym.crm.model.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerMapper {

    private TraineeMapper traineeMapper;

    @Autowired
    public void setTraineeMapper(@Lazy TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    public Trainer toTrainer(TrainerRequest request) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(request.firstName());
        trainer.setLastName(request.lastName());
        trainer.setIsActive(true);
        trainer.setUsername(request.firstName().concat(".").concat(request.lastName()));
        return trainer;
    }

    public TrainerResponse toTrainerResponse(Trainer trainer) {
        List<TraineeResponse> traineeResponses = traineeMapper.toTraineeResponses(trainer.getTrainees());
        return new TrainerResponse(
                trainer.getId(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization().getName(),
                trainer.getIsActive(),
                traineeResponses
        );
    }

    public List<TrainerResponse> toTrainerResponses(List<Trainer> trainers) {
        return trainers.stream()
                .map(this::toTrainerResponse)
                .toList();
    }

    public Trainer toUpdatedTrainer(Trainer trainer, TrainerRequest trainerRequest) {
        trainer.setFirstName(trainerRequest.firstName());
        trainer.setLastName(trainerRequest.lastName());
        trainer.setIsActive(trainerRequest.isActive());
        return trainer;
    }
}
