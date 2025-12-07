package politicConnect.domain;

public enum Badge {

    // 우선순위가 높은 순서대로 배치하는 것이 로직 짤 때 편합니다.
    HOT,        // 참여자 수 1위 (혹은 상위 n%)
    NEW,        // 생성 3일 이내
    TOP_10S,      // 10대 참여 비율 최고
    TOP_20S,      // 20대 참여 비율 최고
    TOP_30S,      // 30대 참여 비율 최고
    TOP_40S,      // 40대 이상
    NORMAL       // 해당 사항 없음
}
