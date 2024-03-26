package ctoutweb.lalamiam.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ctoutweb.lalamiam.model.JwtIssue;
import ctoutweb.lalamiam.security.authentication.UserPrincipal;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@PropertySource({"classpath:application.properties"})
public class JwtIssuer {

  private final Environment environment;

  public JwtIssuer(Environment environment) {
    this.environment = environment;
  }

  public JwtIssue issue(UserPrincipal user) {
    LocalDateTime expiredAt = LocalDateTime.now().plusHours(6);
    byte[] timeNow = ("time now" +" " + System.currentTimeMillis()).getBytes();

    List<String> authorities = user.getAuthorities()
            .stream()
            .map(auth->auth.toString())
            .collect(Collectors.toList());

    String Token = JWT.create()
            .withSubject(user.getUsername())
            .withJWTId(UUID.nameUUIDFromBytes(timeNow).toString())
            .withIssuer(environment.getProperty("jwt.issuer"))
            .withExpiresAt(Instant.from(expiredAt))
            .withClaim("id", user.getId())
            .withClaim("authorities", authorities)
            .sign(Algorithm.HMAC256(environment.getProperty("jwt.secret.key")));
    return new JwtIssue(Token, expiredAt);
  }
}