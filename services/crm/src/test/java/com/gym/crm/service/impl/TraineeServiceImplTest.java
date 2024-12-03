package com.gym.crm.service.impl;

import com.gym.crm.exception.CustomNotFoundException;
import com.gym.crm.mapper.TraineeMapper;
import com.gym.crm.mapper.TrainerMapper;
import com.gym.crm.model.dto.request.TraineeRequest;
import com.gym.crm.model.dto.response.RegistrationResponse;
import com.gym.crm.model.dto.response.TraineeResponse;
import com.gym.crm.model.entity.Trainee;
import com.gym.crm.model.entity.Trainer;
import com.gym.crm.repository.TraineeRepository;
import com.gym.crm.repository.TrainerRepository;
import com.gym.crm.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void create_Success() {
        TraineeRequest traineeRequest = new TraineeRequest(
                "Jim", "Rohn", LocalDate.of(2000, 1, 1), "USA", true
        );
        Trainee trainee = Trainee.builder()
                .firstName(traineeRequest.firstName())
                .lastName(traineeRequest.lastName())
                .dateOfBirth(traineeRequest.dateOfBirth())
                .address(traineeRequest.address())
                .isActive(traineeRequest.isActive())
                .username("Jim.Rohn")
                .build();

        when(traineeMapper.toTrainee(traineeRequest)).thenReturn(trainee);
        when(traineeRepository.existsTraineeByUsername(trainee.getUsername())).thenReturn(false);

        RegistrationResponse registrationResponse = traineeService.create(traineeRequest);

        assertEquals(trainee.getUsername(), registrationResponse.username());

        verify(traineeMapper, times(1)).toTrainee(traineeRequest);
        verify(traineeRepository, times(1)).existsTraineeByUsername(any());
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    void testCreate_WhenUsernameExists_ShouldChangeUsername() {
        TraineeRequest request = new TraineeRequest("Jim", "Rohn", LocalDate.now(), "USA", true);
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername("Jim.Rohn1");

        when(traineeMapper.toTrainee(request)).thenReturn(trainee);
        when(traineeRepository.existsTraineeByUsername(trainee.getUsername())).thenReturn(true);

        RegistrationResponse response = traineeService.create(request);
        assertEquals(response.username(), trainee.getUsername());

        verify(traineeMapper, times(1)).toTrainee(request);
        verify(traineeRepository, times(1)).existsTraineeByUsername(anyString());
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    void updateUpdateSuccess() {
        TraineeRequest request = new TraineeRequest("Jimmy", "Rohn", LocalDate.now(), "USA", true);
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        Trainee updatedTrainee = new Trainee("Jimmy", "Rohn", true, LocalDate.now(), "USA");
        TraineeResponse traineeResponse = new TraineeResponse(1L, "Jimmy", "Rohn", LocalDate.now(), "USA", true, null);

        when(traineeRepository.findByUsername("Jim.Rohn")).thenReturn(Optional.of(trainee));
        when(traineeMapper.toUpdatedTrainee(trainee, request)).thenReturn(updatedTrainee);
        when(traineeMapper.toTraineeResponse(updatedTrainee)).thenReturn(traineeResponse);

        TraineeResponse response = traineeService.update("Jim.Rohn", request);

        assertEquals(traineeResponse, response);

        verify(traineeRepository, times(1)).findByUsername("Jim.Rohn");
        verify(traineeMapper, times(1)).toUpdatedTrainee(trainee, request);
        verify(traineeRepository, times(1)).save(updatedTrainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testUpdateNotFound() {
        String username = "Jim.Rohn";
        TraineeRequest request = new TraineeRequest("Jimmy", "Rohn", LocalDate.now(), "USA", true);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.update(username, request);
        });

        assertEquals("Trainee with id %s not found".formatted(username), exception.getMessage());

        verify(traineeRepository, never()).save(any(Trainee.class));
        verify(traineeMapper, never()).toUpdatedTrainee(any(), any());
    }

    @Test
    public void testDelete_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setTrainers(new ArrayList<>());

        Trainer trainer = new Trainer(null, new ArrayList<>(), new ArrayList<>());
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        traineeService.delete(username);

        verify(traineeRepository, times(1)).deleteByUsername(username);
        verify(trainingRepository, times(1)).deleteByTraineeUsername(username);
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void testDelete_WhenUserNotFound_Fails() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.delete(username);
        });

        assertEquals("Trainee with username %s not found".formatted(username), exception.getMessage());

        verify(traineeRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    void findByUsername_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);

        TraineeResponse traineeResponse = new TraineeResponse(null, "Jim", "Rohn", LocalDate.now(), "USA", true, null);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeMapper.toTraineeResponse(trainee)).thenReturn(traineeResponse);

        TraineeResponse response = traineeService.findByUsername(username);

        assertEquals(traineeResponse, response);

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeMapper, times(1)).toTraineeResponse(trainee);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    public void testFindByUsername_NotFound() {
        when(traineeRepository.findByUsername("username")).thenReturn(Optional.empty());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.findByUsername("username");
        });

        assertEquals("Trainee with username %s not found".formatted("username"), exception.getMessage());
        verify(traineeRepository).findByUsername("username");
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testFindAllSuccess() {
        List<Trainee> trainees = List.of(new Trainee());
        List<TraineeResponse> traineeResponses = List.of(new TraineeResponse(null, "Jim", "Rohn", LocalDate.now(), "USA", true,null));

        when(traineeRepository.findAll()).thenReturn(trainees);
        when(traineeMapper.toTraineeResponses(trainees)).thenReturn(traineeResponses);

        List<TraineeResponse> response = traineeService.findAll();

        assertEquals(traineeResponses, response);

        verify(traineeRepository, times(1)).findAll();
        verify(traineeMapper, times(1)).toTraineeResponses(trainees);
        verifyNoMoreInteractions(traineeMapper, traineeRepository);
    }

    @Test
    public void testFindAll_NoTrainees() {
        when(traineeRepository.findAll()).thenReturn(Collections.emptyList());
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
            traineeService.findAll();
        });
        assertEquals("Trainees not found!", exception.getMessage());
        verify(traineeRepository, times(1)).findAll();
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testDeleteAll_Success() {
        traineeService.deleteAll();
        verify(traineeRepository, times(1)).deleteAll();
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testUpdatePassword_Success() {
        String username = "Jim.Rohn";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setPassword(oldPassword);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.updatePassword(username, oldPassword, newPassword);

        assertEquals(newPassword, trainee.getPassword());
        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testUpdatePassword_IncorrectOldPassword() {
        String username = "Jim.Rohn";
        String oldPassword = "wrongPassword";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setPassword("correctOldPassword");

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> traineeService.updatePassword(username, oldPassword, newPassword)
        );

        assertEquals("Update password operation failed", exception.getMessage());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, never()).save(any(Trainee.class));
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testDeActivateUser_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(true);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.deActivateUser(username);

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testDeActivateUser_Fails() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(false);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> traineeService.deActivateUser(username)
        );

        assertEquals("User already inactive", exception.getMessage());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(0)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testActivateUser_Success() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);
        trainee.setIsActive(false);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        traineeService.activateUser(username);

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(1)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    public void testActivateUser_Fails() {
        String username = "Jim.Rohn";
        Trainee trainee = new Trainee("Jim", "Rohn", true, LocalDate.now(), "USA");
        trainee.setUsername(username);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> traineeService.activateUser(username)
        );

        assertEquals("User already active", exception.getMessage());

        verify(traineeRepository, times(1)).findByUsername(username);
        verify(traineeRepository, times(0)).save(trainee);
        verifyNoMoreInteractions(traineeRepository);
    }
}