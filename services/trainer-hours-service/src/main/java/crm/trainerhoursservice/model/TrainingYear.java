package crm.trainerhoursservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TrainingYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "training_year")
    private Integer year;

    private Double totalDuration;

    @JsonIgnore
    @ManyToOne
    private TrainingHour trainingHour;

    @OneToMany(mappedBy = "trainingYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingMonth> months = new ArrayList<>();


    public TrainingYear addMonth(TrainingMonth month) {
        if (this.months == null) {
            this.months = new ArrayList<>();
        }
        this.months.add(month);
        month.setTrainingYear(this);
        return this;
    }

}
