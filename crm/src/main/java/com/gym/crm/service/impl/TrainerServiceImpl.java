package com.gym.crm.service.impl;

import com.gym.crm.exception.CustomNotFoundException;
import com.gym.crm.mapper.TrainerMapper;
import com.gym.crm.model.dto.request.RegisterRequest;
import com.gym.crm.model.dto.request.TrainerRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.model.dto.response.TrainerResponse;
import com.gym.crm.model.entity.Trainer;
import com.gym.crm.model.entity.TrainingType;
import com.gym.crm.repository.TrainerRepository;
import com.gym.crm.repository.TrainingTypeRepository;
import com.gym.crm.service.TrainerService;
import com.gym.crm.service.client.AuthClient;
import com.gym.crm.util.PasswordGenerator;
import com.gym.crm.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;

    private final TrainerRepository trainerRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    private final AuthClient authClient;

    @Override
    public RegistrationResponse create(TrainerRequest trainerRequest) {
        log.debug("Creating new trainer with request: {}", trainerRequest);
        Trainer trainer = trainerMapper.toTrainer(trainerRequest);
        trainer.setPassword(PasswordGenerator.generatePassword());
        TrainingType trainingType = trainingTypeRepository.findById(trainerRequest.specializationId())
                .orElseThrow(() -> new CustomNotFoundException("TrainingType with id : %d not found".formatted(
                        trainerRequest.specializationId()))
                );

        trainer.setSpecialization(trainingType);
        if (trainerRepository.existsTrainerByUsername(trainer.getUsername())) {
            trainer.setUsername(trainer.getUsername() +  Utils.trainerIdx++);
            log.info("Username already exists, changed it to {}", trainer.getUsername());
        }
        RegistrationResponse registrationResponse = new RegistrationResponse(trainer.getUsername(), trainer.getPassword());
        trainerRepository.save(trainer);

        authClient.register(new RegisterRequest(trainer.getUsername(), trainer.getPassword()));

        log.info("Trainer saved successfully: {}", trainer);
        return registrationResponse;
    }

    @Override
    public TrainerResponse update(String username, TrainerRequest trainerRequest) {
        log.debug("Updating trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername (username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        Trainer updated = trainerMapper.toUpdatedTrainer(trainer, trainerRequest);
        TrainingType trainingType = trainingTypeRepository.findById(trainerRequest.specializationId())
                .orElseThrow(() -> new CustomNotFoundException("TrainingType with id : %d not found".formatted(
                        trainerRequest.specializationId()))
                );

        updated.setSpecialization(trainingType);
        trainerRepository.save(updated);

        TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(updated);

        log.info("Trainer updated successfully: {}", updated);
        return trainerResponse;
    }

    @Override
    public TrainerResponse findByUsername(String username) {
        log.debug("Finding trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        TrainerResponse trainerResponse = trainerMapper.toTrainerResponse(trainer);
        log.info("Trainer found: {}", trainerResponse);
        return trainerResponse;
    }

    @Override
    public List<TrainerResponse> findAll() {
        log.debug("Finding all trainers");
        List<Trainer> trainers = trainerRepository.findAll();
        if (trainers.isEmpty()) {
            log.error("No trainers found!");
            throw new CustomNotFoundException("Trainers not found!");
        }
        List<TrainerResponse> trainerResponses = trainerMapper.toTrainerResponses(trainers);
        log.info("Found {} trainers", trainerResponses.size());
        return trainerResponses;
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        log.debug("Updating password for trainer {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        if (!Objects.equals(trainer.getPassword(), oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        trainer.setPassword(newPassword);
        trainerRepository.save(trainer);
        log.info("Password updated successfully for trainer {}", username);
    }

    @Override
    public void deActivateUser(String username) {
        log.debug("Deactivating trainer with username {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        if (!trainer.getIsActive()) {
            log.warn("Trainer {} is already inactive", username);
            throw new IllegalArgumentException("Trainer is already inactive");
        }
        trainer.setIsActive(false);
        trainerRepository.save(trainer);
        log.info("Trainer {} deactivated successfully", username);
    }

    @Override
    public void activateUser(String username) {
        log.debug("Activating trainer with username {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found!"));

        if (trainer.getIsActive()) {
            log.warn("Trainer {} is already active", username);
            throw new IllegalArgumentException("Trainer is already active");
        }
        trainer.setIsActive(true);
        trainerRepository.save(trainer);
        log.info("Trainer {} activated successfully", username);
    }

    @Override
    public boolean isDBEmpty() {
        return trainerRepository.isEmpty();
    }

    @Override
    public Trainer one(String trainerUsername) {
        return trainerRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new CustomNotFoundException("Trainer not found"));
    }
}
