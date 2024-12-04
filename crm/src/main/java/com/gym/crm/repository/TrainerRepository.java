package com.gym.crm.repository;

import com.gym.crm.model.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    boolean existsTrainerByUsername(String username);

    Optional<Trainer> findByUsername(String username);

    @Query("from Trainer t left join fetch t.trainees where t.id = :trainerId")
    Optional<Trainer> findByIdWithTrainees(Long trainerId);

    @Query("select count(t) = 0 from Trainer t")
    boolean isEmpty();
}
