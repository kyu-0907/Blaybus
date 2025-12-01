package politicConnect.auth;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import politicConnect.domain.Provider;
import politicConnect.dto.*;

@RestController
@RequestMapping("auth/")
@RequiredArgsConstructor
public class AuthController {

    private final LocalService localService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final GoogleService googleService;
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

    // ✅ :code를 받아와서 기존 유저인지 체크. 기존 유저라면 토큰을 발급하여 로그인 처리 , 아니라면 회원가입 api 로 이동 (provider를 가지고 있는 상태로)
    @PostMapping("/kakao")
    public ResponseEntity<SocialLoginResponse> checkKakao(@RequestParam("code") String code) {
        SocialLoginResponse res = kakaoService.handleKakaoLogin(code);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/naver")
    public ResponseEntity<SocialLoginResponse> checkNaver(@RequestParam("code") String code) {
        SocialLoginResponse res = naverService.handleNaverLogin(code);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/google")
    public ResponseEntity<SocialLoginResponse> checkGoogle(@RequestParam("code") String code) {
        SocialLoginResponse res = googleService.handleGoogleLogin(code);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/social/signup")
    public ResponseEntity<TokenDto> signupKakao(@RequestBody SocialSignUpRequest req) {
        TokenDto tokenDto = socialLoginService.completeSignup(req);
        return ResponseEntity.ok(tokenDto);
    }




    /*
    // ✅ [NAVER] 2단계: 회원가입
    @PostMapping("/naver/signup")
    public ResponseEntity<SocialLoginResponse> signupNaver(@RequestBody SocialSignupRequest req) {
        SocialLoginResponse res = naverService.signupWithNaver(req);
        return ResponseEntity.ok(res);
    }

    // ✅ [GOOGLE] 2단계: 회원가입
    @PostMapping("/google/signup")
    public ResponseEntity<SocialLoginResponse> signupGoogle(@RequestBody SocialSignupRequest req) {
        SocialLoginResponse res = googleService.signupWithGoogle(req);
        return ResponseEntity.ok(res);
    }

     */
}





















}
