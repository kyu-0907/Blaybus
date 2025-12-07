package politicConnect.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SurveyDto {

    private Long id;
    private String title;
    private Integer estimatedTimeInMinutes;

    private LocalDateTime startDate;
    private LocalDateTime endDate;




}
