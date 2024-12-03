package crm.trainerhoursservice.repository;

import crm.trainerhoursservice.model.TrainingYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingYearRepository extends JpaRepository<TrainingYear, Integer> {

    Optional<TrainingYear> findByYear(Integer year);

}
