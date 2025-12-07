package politicConnect.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 보호
@Table(name = "surveys") // 테이블명 지정
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 설문 제목
    @Column(nullable = false)
    private String title;

    // 카테고리 (정치, 사회, 청년 경제)
    // EnumType.STRING을 써야 DB에 순서(0,1,2)가 아닌 텍스트로 저장되어 안전합니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurveyCategory surveyCategory;

    // 소요 시간 (분 단위 권장, 예: 5분 -> 5)
    private Integer estimatedTimeInMinutes;

    // 진행 기간 (시작일)
    private LocalDateTime startDate;

    // 진행 기간 (종료일)
    private LocalDateTime endDate;

    // 참여율 (퍼센트, 예: 55.5%)
    // 참고: 실제로는 (참여자 수 / 전체 대상 수)로 계산하는 경우가 많으나,
    // 요청하신 대로 값을 저장하는 필드로 만들었습니다.
    private Double participationRate;

    private String Badge;



    // 생성자 (Builder 패턴 사용)
    @Builder
    public Survey(String title, SurveyCategory category, Integer estimatedTimeInMinutes,
                  LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.surveyCategory = category;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participationRate = 0.0; // 초기 참여율은 0%로 설정
    }

    // 참여율 업데이트를 위한 메서드 (비즈니스 로직)
    public void updateParticipationRate(Double newRate) {
        this.participationRate = newRate;
    }
}
