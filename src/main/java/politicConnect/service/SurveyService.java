package politicConnect.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import politicConnect.domain.SurveyCategory;
import politicConnect.dto.SurveyDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    List<SurveyDto> getSurveyDtoList(SurveyCategory surveyCategory) {
        List<SurveyDto> surveyDtoList = new ArrayList<>();


    }
}
