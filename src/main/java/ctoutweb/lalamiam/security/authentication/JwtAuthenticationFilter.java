package ctoutweb.lalamiam.security.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import ctoutweb.lalamiam.exception.AuthException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.security.jwt.JwtDecode;
import ctoutweb.lalamiam.security.jwt.JwtToUserPrincipal;
import ctoutweb.lalamiam.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  // DecodedJwt
  Map<String, DecodedJWT> decodedJWTMap = new HashMap<>();
  private final JwtDecode jwtDecode;
  private final JwtToUserPrincipal jwtToUserPrincipal;
  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtDecode jwtDecode, JwtToUserPrincipal jwtToUserPrincipal, JwtService jwtService) {
    this.jwtDecode = jwtDecode;
    this.jwtToUserPrincipal = jwtToUserPrincipal;
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException, AuthException {
    try {
      extractJwtFromHeaders(request)
        .map(token->{
          decodedJWTMap.put("decodeJwt", jwtDecode.decode(token));
          return decodedJWTMap.get("decodeJwt");
        })
        .map(decodeJwt-> validateJwt())
        .map(decodeJwt-> jwtToUserPrincipal.convert(decodeJwt))
        .map(userPrincipal-> Factory.getUserPrincipalFromUserAuthToken(userPrincipal))
        .ifPresentOrElse(auth-> SecurityContextHolder.getContext().setAuthentication(auth), () -> {
          throw new AuthException("echec validation JWT", HttpStatus.FORBIDDEN);
        });
    }
    catch (Exception ex) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Validation JWT -> jwtId présent en base de données et appartient au UserId
   * @return DecodedJWT
   * @throws AuthException
   */
  private DecodedJWT validateJwt() throws AuthException {

    DecodedJWT jwt = (DecodedJWT) decodedJWTMap.get("decodeJwt");

    Long userId = jwt.getClaim("id").asLong();

    String jwtToken = jwt.getToken();

    String jwtId = jwt.getId();

    if(!jwtService.isJwtValid(userId, jwtToken, jwtId))
      throw new AuthException("echec validation JWT", HttpStatus.UNAUTHORIZED);

    return jwt;
  }

  /**
   * Récupération du JWT
   * @param request HttpServletRequest
   * @return Optional<String>
   */
  private Optional<String> extractJwtFromHeaders(HttpServletRequest request) {
    // Todo faire test
    var token = request.getHeader("authorization");
    if(!StringUtils.hasText(token) || !token.startsWith("Bearer ")) return Optional.empty();
    return Optional.of(token.substring(7));
  }
}
