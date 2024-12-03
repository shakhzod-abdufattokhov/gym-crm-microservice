package crm.trainerhoursservice.service;

import crm.trainerhoursservice.model.*;
import crm.trainerhoursservice.repository.TrainerHourRepository;
import crm.trainerhoursservice.repository.TrainingMonthRepository;
import crm.trainerhoursservice.repository.TrainingYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadService {

    private final TrainingMonthRepository monthRepository;
    private final TrainingYearRepository yearRepository;
    private final TrainerHourRepository trainerHourRepository;

    public void handleWorkload(TrainerWorkload workload) {
        TrainingHour trainingHour = trainerHourRepository.findByTrainerUsername(workload.username())
                .orElse(
                        TrainingHour.builder()
                                .trainerUsername(workload.username())
                                .firstName(workload.firstName())
                                .lastName(workload.lastName())
                                .build()
                );

        TrainingMonth trainingMonth = monthRepository.findByMonth(workload.trainingDate().getMonth())
                .orElse(null);

        TrainingYear trainingYear = yearRepository.findByYear(workload.trainingDate().getYear())
                .orElse(null);

        if (trainingYear == null) {
            TrainingMonth month = TrainingMonth.builder()
                    .month(workload.trainingDate().getMonth())
                    .durationInHour(workload.trainingDuration())
                    .build();

            TrainingYear year = TrainingYear.builder()
                    .year(workload.trainingDate().getYear())
                    .totalDuration(workload.trainingDuration())
                    .build();

            year.addMonth(month);

            trainingHour.addTrainingYear(year);
            trainerHourRepository.save(trainingHour);
            return;
        }

        if (trainingMonth == null) {
            TrainingMonth month = TrainingMonth.builder()
                    .month(workload.trainingDate().getMonth())
                    .durationInHour(workload.trainingDuration())
                    .build();

            trainingYear.setTotalDuration(trainingYear.getTotalDuration() + workload.trainingDuration());
            yearRepository.save(trainingYear.addMonth(month));
            return;
        }

        trainingMonth.setDurationInHour(trainingMonth.getDurationInHour() + workload.trainingDuration());
        trainingYear.setTotalDuration(trainingYear.getTotalDuration() + workload.trainingDuration());
        yearRepository.save(trainingYear);

    }


    public TrainingHour summarize(String trainerUsername) {
        TrainingHour trainingHour = trainerHourRepository.findByTrainerUsername(trainerUsername)
                .orElse(null);
        return trainingHour;
    }
}
