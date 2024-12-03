//package com.gym.crm.util;
//
//import com.gym.crm.model.dto.request.TraineeRequest;
//import com.gym.crm.model.dto.request.TrainerRequest;
//import com.gym.crm.model.dto.request.TrainingRequest;
//import com.gym.crm.model.dto.request.TrainingTypeRequest;
//import com.gym.crm.model.entity.Trainee;
//import com.gym.crm.model.entity.Trainer;
//import com.gym.crm.model.entity.TrainingType;
//import com.gym.crm.service.TraineeService;
//import com.gym.crm.service.TrainerService;
//import com.gym.crm.service.TrainingService;
//import com.gym.crm.service.TrainingTypeService;
//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvException;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    @Value("${storage.trainee.file}")
//    private String traineeDataFile;
//
//    @Value("${storage.trainer.file}")
//    private String trainerDataFile;
//
//    @Value("${storage.training.file}")
//    private String trainingDataFile;
//
//    @Value("${storage.training-type.file}")
//    private String trainingTypeDataFile;
//
//    private final TraineeService traineeService;
//    private final TrainerService trainerService;
//    private final TrainingService trainingService;
//    private final TrainingTypeService trainingTypeService;
//
//    @PostConstruct
//    public void initAll() throws InterruptedException {
//        log.info("Initializing data...");
//        initTrainingType();
//        initTrainer();
//        initTrainee();
//        initTraining();
//        log.info("Data initialization complete.");
//    }
//
//    public void initTrainingType() {
//        if (!trainingTypeService.isDBEmpty()){
//            return;
//        }
//
//        log.info("Initializing training type data from file...");
//        Resource resource = new ClassPathResource(trainingTypeDataFile);
//
//        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
//            List<String[]> rows = reader.readAll();
//            for (String[] parts : rows) {
//                if (parts.length == 1) {
//                    String trainingTypeName = parts[0];
//
//                    TrainingTypeRequest trainingTypeRequest = new TrainingTypeRequest(trainingTypeName);
//
//                    trainingTypeService.createTrainingType(trainingTypeRequest);
//                    log.info("Training type created: {}", trainingTypeName);
//                } else {
//                    log.info("Skipping invalid trainee row: {}", String.join(",", parts));
//                }
//            }
//        } catch (IOException | CsvException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void initTrainee() {
//        if (!traineeService.isDBEmpty()) {
//            log.info("Trainees are already initialized.");
//            return;
//        }
//
//        log.info("Initializing trainee data from file...");
//        Resource resource = new ClassPathResource(traineeDataFile);
//
//        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
//            List<String[]> rows = reader.readAll();
//            for (int rowNum = 0; rowNum < rows.size(); rowNum++) {
//                String[] parts = rows.get(rowNum);
//                if (parts.length < 5) {
//                    log.warn("Skipping invalid row {}: {}", rowNum + 1, String.join(",", parts));
//                    continue;
//                }
//
//                try {
//                    String firstName = parts[0].trim();
//                    String lastName = parts[1].trim();
//                    LocalDate dateOfBirth = LocalDate.parse(parts[2].trim(), FORMATTER);
//                    String address = parts[3].trim();
//                    boolean isActive = Boolean.parseBoolean(parts[4].trim());
//
//                    TraineeRequest traineeRequest = new TraineeRequest(
//                            firstName, lastName, dateOfBirth, address, isActive
//                    );
//
//                    traineeService.create(traineeRequest);
//                    log.info("Trainee created: {} {}", firstName, lastName);
//                } catch (Exception e) {
//                    log.error("Error processing row {}: {}", rowNum + 1, String.join(",", parts), e);
//                }
//            }
//        } catch (IOException | CsvException e) {
//            log.error("Error reading trainee file: {}", traineeDataFile, e);
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public void initTrainer() {
//        if (!trainerService.isDBEmpty()) {
//            return;
//        }
//
//        log.info("Initializing trainer data from file...");
//        Resource resource = new ClassPathResource(trainerDataFile);
//
//        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
//            List<String[]> rows = reader.readAll();
//            for (String[] parts : rows) {
//                if (parts.length >= 3) {
//                    String firstName = parts[0];
//                    String lastName = parts[1];
//                    String specializationName = parts[2];
//                    boolean isActive = Boolean.parseBoolean(parts[3]);
//
//                    TrainingType trainingType = trainingTypeService.findByName(specializationName);
//
//                    TrainerRequest trainer = new TrainerRequest(
//                            firstName,
//                            lastName,
//                            trainingType.getId(),
//                            isActive
//                    );
//                    trainerService.create(trainer);
//                    log.info("Trainer created: {}", trainer);
//                } else {
//                    log.warn("Skipping invalid trainer row: {}", String.join(",", parts));
//                }
//            }
//        } catch (IOException | CsvException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void initTraining() {
//        if (!trainingService.isDBEmpty()){
//            return;
//        }
//
//        log.info("Initializing training data from file");
//        Resource resource = new ClassPathResource(trainingDataFile);
//
//        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
//            List<String[]> rows = reader.readAll();
//            for (String[] parts : rows) {
//                if (parts.length >= 6) {
//                    String traineeUsername = parts[0];
//                    String trainerUsername = parts[1];
//                    String trainingName = parts[2];
//                    String trainingTypeName = parts[3];
//                    LocalDate trainingDate = LocalDate.parse(parts[4], FORMATTER);
//                    Duration duration = Duration.parse(parts[5]);
//
//                    TrainingType trainingType = trainingTypeService.findByName(trainingTypeName);
//
//                    Trainer trainer = trainerService.one(trainerUsername);
//
//                    Trainee trainee = traineeService.one(traineeUsername);
//
//                    TrainingRequest training = new TrainingRequest(
//                            trainee.getId(),
//                            trainer.getId(),
//                            trainingName,
//                            trainingType.getId(),
//                            trainingDate,
//                            duration
//                    );
//
//                    log.info("creating training: {x}", training);
//                    trainingService.create(training);
//                    log.info("training created: {}", training);
//                } else {
//                    log.warn("Skipping invalid training row: {}", String.join(",", parts));
//                }
//            }
//        } catch (IOException | CsvException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
