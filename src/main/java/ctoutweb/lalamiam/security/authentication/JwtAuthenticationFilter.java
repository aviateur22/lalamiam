package ctoutweb.lalamiam.security.authentication;

import ctoutweb.lalamiam.exception.AuthException;
import ctoutweb.lalamiam.security.jwt.JwtDecode;
import ctoutweb.lalamiam.security.jwt.JwtToUserPrincipal;
import ctoutweb.lalamiam.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

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
          throws ServletException, IOException {
    extractTokenFromHeaders(request)
            .map(token->jwtDecode.decode(token))
            .map(decodedJwt->jwtToUserPrincipal.convert(decodedJwt))
            .map(userPrincipal -> new UserPrincipalAuthenticationToken(userPrincipal))
            .ifPresent(auth->{
              SecurityContextHolder.getContext().setAuthentication(auth);
              this.validateJwt(request);
            });
            filterChain.doFilter(request, response);
  }

  private void validateJwt(HttpServletRequest request) {
    // Todo faire test
    UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String jwtToken = extractTokenFromHeaders(request).get();
    if(!jwtService.isJwtValid(user.getId(), jwtToken))
      throw new AuthException("le token n'est pas valid", HttpStatus.FORBIDDEN);
  }
  private Optional<String> extractTokenFromHeaders(HttpServletRequest request) {
    // Todo faire test
    var token = request.getHeader("authorization");
    if(!StringUtils.hasText(token) || !token.startsWith("Bearer ")) return Optional.empty();
    return Optional.of(token.substring(7));
  }
}
