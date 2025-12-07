package politicConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import politicConnect.domain.Survey;
import politicConnect.domain.SurveyCategory;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Optional<Survey> findBySurveyCategory(SurveyCategory surveyCategory);
}
