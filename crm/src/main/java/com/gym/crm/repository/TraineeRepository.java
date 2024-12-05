package com.gym.crm.repository;

import com.gym.crm.model.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    boolean existsTraineeByUsername(String username);

//    @Query("from Trainee t join fetch t.trainers where t.username = :username")
    Optional<Trainee> findByUsername(String username);

    void deleteByUsername(String username);

    @Query("from Trainee t left join fetch t.trainers where t.id = :traineeId")
    Optional<Trainee> findByIdWithTrainers(Long traineeId);

    @Query("select COUNT(t) = 0 from Trainee t")
    boolean isEmpty();
}
