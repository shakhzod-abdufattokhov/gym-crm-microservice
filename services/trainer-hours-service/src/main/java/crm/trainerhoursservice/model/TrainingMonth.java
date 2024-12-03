package crm.trainerhoursservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Month;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TrainingMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_month", columnDefinition = "VARCHAR(20)")
    private Month month;

    private Double durationInHour;

    @JsonIgnore
    @ManyToOne
    private TrainingYear trainingYear;
}
