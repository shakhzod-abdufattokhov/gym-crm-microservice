package com.gym.crm.repository;

import com.gym.crm.model.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

    Optional<TrainingType> findByName(String name);

    @Query("select count(t) = 0 from TrainingType t")
    boolean isEmpty();
}
