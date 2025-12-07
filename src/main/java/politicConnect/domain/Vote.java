package politicConnect.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor

public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // 소요 시간 (분 단위 권장, 예: 5분 -> 5)
    private Integer estimatedTimeInMinutes;

    @Enumerated(EnumType.STRING)
    private VoteCategory voteCategory;

    // 진행 기간 (시작일)
    private LocalDateTime startDate;

    // 진행 기간 (종료일)
    private LocalDateTime endDate;

    private Badge badge;
}
