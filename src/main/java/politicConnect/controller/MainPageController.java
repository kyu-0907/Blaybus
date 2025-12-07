package politicConnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import politicConnect.dto.MainPageDto;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    @GetMapping("/main")
    ResponseEntity<MainPageDto> getMainPage() {
        return ResponseEntity.ok(mainPageService.getMain);

    }



}
