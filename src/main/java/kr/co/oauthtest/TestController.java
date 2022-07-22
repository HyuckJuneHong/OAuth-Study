package kr.co.oauthtest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JwtProvider jwtProvider;

    @GetMapping("/test")
    public String test(){
        return "test success";
    }

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("RefreshToken");

        if (token != null && jwtProvider.verifyToken(token)) {
            String email = jwtProvider.getUid(token);
            TokenDto newToken = jwtProvider.getTokenDto(email, "USER");
            response.addHeader("Authentication", newToken.getAccessToken());
            response.addHeader("RefreshToken", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "NEW TOKEN";
        }

        throw new RuntimeException();
    }
}
