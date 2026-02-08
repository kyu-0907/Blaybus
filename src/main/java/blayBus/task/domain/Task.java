package blayBus.task.domain;

import blayBus.material.domain.Material;
import blayBus.planner.domain.Planner;
import blayBus.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id")
    private Planner planner;

    private String title;

    private String goal;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "task")
    private List<Material> materials = new ArrayList<>();

    public static Task create(TaskInfo taskInfo) {
        Task task = new Task();

        task.planner = taskInfo.planner();
        task.title = taskInfo.title();
        task.goal = taskInfo.goal();
        task.subject = taskInfo.subject();
        task.status = TaskStatus.TODO;
        task.author = taskInfo.author();

        return task;
    }
}