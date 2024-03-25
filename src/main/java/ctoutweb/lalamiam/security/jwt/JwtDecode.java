package ctoutweb.lalamiam.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:application.properties"})
public class JwtDecode {

  private final Environment environment;

  public JwtDecode(Environment environment) {
    this.environment = environment;
  }

  public DecodedJWT decode(String token) {
    return JWT.require(Algorithm.HMAC256(environment.getProperty("jwt.secret.key")))
            .build()
            .verify(token);
  }
}
