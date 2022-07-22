package kr.co.oauthtest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtProvider {

    private String SECRET_KEY = "token-secret-key";
    private final long ACCESS_EXPIRE = 1000 * 60 * 30;              //30분
    private final long REFRESH_EXPIRE = 1000 * 60 * 60 * 24 * 14;   //2주

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    private Claims generateClaims(String uid, String role){
        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        return claims;
    }

    public String createAccessToken(String uid, String role){
        Date issueDate = new Date();    //토큰 발행 시각.
        Date expireDate = new Date();   //토큰 유효 시각.
        expireDate.setTime(issueDate.getTime() + ACCESS_EXPIRE);

        return Jwts.builder()
                .setClaims(generateClaims(uid, role))
                .setIssuedAt(issueDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String createRefreshToken(String uid, String role){
        Date issueDate = new Date();
        Date expireDate = new Date();
        expireDate.setTime(issueDate.getTime() + REFRESH_EXPIRE);

        return Jwts.builder()
                .setClaims(generateClaims(uid, role))
                .setIssuedAt(issueDate)
                .setSubject("RefreshToken")     //토큰 용도 다름.
                .setExpiration(expireDate)      //유효 시간 2주일.
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public TokenDto getTokenDto(String uid, String role){
        return new TokenDto(createAccessToken(uid, role), createRefreshToken(uid, role));
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUid(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}

