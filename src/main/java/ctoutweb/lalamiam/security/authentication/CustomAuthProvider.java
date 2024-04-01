package ctoutweb.lalamiam.security.authentication;

import ctoutweb.lalamiam.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  public CustomAuthProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthException {
    if (authentication.getCredentials() == null)
      throw new AuthException("identifiant ou mot de passe invalide ", HttpStatus.BAD_REQUEST);

    String presentedPassword = authentication.getCredentials().toString();
    UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());

    if (!this.passwordEncoder.matches(presentedPassword, user.getPassword()))
      throw new AuthException("identifiant ou mot de passe invalide", HttpStatus.BAD_REQUEST);

    return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials().toString());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
