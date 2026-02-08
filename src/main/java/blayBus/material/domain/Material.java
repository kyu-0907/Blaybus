package blayBus.material.domain;

import blayBus.task.domain.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MaterialType type;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    private Material(MaterialType type, String url, Task task) {
        this.type = type;
        this.url = url;
        this.task = task;
    }

    public static Material of(MaterialType type, String url, Task task) {
        Material material = new Material(type, url, task);
        if (task != null) {
            task.getMaterials().add(material);
        }
        return material;
    }
}
