package crm.trainerhoursservice.repository;

import crm.trainerhoursservice.model.TrainingMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.Optional;

@Repository
public interface TrainingMonthRepository extends JpaRepository<TrainingMonth, Integer> {

    Optional<TrainingMonth> findByMonth(Month month);

}
