package com.gym.crm.service.impl;

import com.gym.crm.exception.CustomAlreadyExistException;
import com.gym.crm.exception.CustomNotFoundException;
import com.gym.crm.model.dto.request.TrainingTypeRequest;
import com.gym.crm.model.entity.TrainingType;
import com.gym.crm.repository.TrainingTypeRepository;
import com.gym.crm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainingType findByName(String name) {
        return trainingTypeRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Training type with name is not found"));
    }

    @Override
    public void createTrainingType(TrainingTypeRequest trainingTypeRequest) {
        Optional<TrainingType> byName = trainingTypeRepository.findByName(trainingTypeRequest.name());
        if(byName.isPresent()) {
            throw new CustomAlreadyExistException("Training Type is already exist with this name: "+
                    trainingTypeRequest.name());
        }
        trainingTypeRepository.save(new TrainingType(trainingTypeRequest.name()));
    }

    @Override
    public boolean isDBEmpty() {
        return trainingTypeRepository.isEmpty();
    }
}
