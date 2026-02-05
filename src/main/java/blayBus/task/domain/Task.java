package blayBus.task.domain;

import blayBus.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "planner_id")
    private Planner planner;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }
}