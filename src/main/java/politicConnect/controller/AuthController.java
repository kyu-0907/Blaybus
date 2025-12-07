package politicConnect.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import politicConnect.auth.*;
import politicConnect.dto.*;

@RestController
@RequestMapping("auth/")
@RequiredArgsConstructor
public class AuthController {

    private final LocalService localService;
    private final SocialLoginService socialLoginService;

    @PostMapping("local/signUp")
    public ResponseEntity<String> localSignup(@Valid @RequestBody LocalSignupRequest request) {

        localService.localSignUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("회원가입이 완료되었습니다");
    }

    @PostMapping("local/Login")
    public ResponseEntity<TokenDto> localLogin(@Valid @RequestBody LocalLoginRequest request) {

        TokenDto tokenDto = localService.localLogin(request);

        return ResponseEntity.ok(tokenDto);

    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam String localLoginId) {

        boolean duplicated = localService.isLocalLoginIdDuplicated(localLoginId);

        return ResponseEntity.ok(duplicated);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickName(
            @RequestParam String nickName) {

        boolean duplicated = localService.isNickNameDuplicated(nickName);
        return ResponseEntity.ok(duplicated);
    }
}

