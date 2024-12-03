package crm.trainerhoursservice.repository;

import crm.trainerhoursservice.model.TrainingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerHourRepository extends JpaRepository<TrainingHour, Integer> {

    Optional<TrainingHour> findByTrainerUsername(String trainerUsername);

}
