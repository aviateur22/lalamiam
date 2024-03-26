package ctoutweb.lalamiam.security.authentication;

import ctoutweb.lalamiam.mapper.SimpleGrantedAuthorityMapper;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import ctoutweb.lalamiam.repository.transaction.UserTransactionSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailService implements UserDetailsService {

  private final UserTransactionSession userTransactionSession;
  private final SimpleGrantedAuthorityMapper simpleGrantedAuthorityMapper;

  public UserPrincipalDetailService(
          UserTransactionSession userTransactionSession,
          SimpleGrantedAuthorityMapper simpleGrantedAuthorityMapper
  ) {
    this.userTransactionSession = userTransactionSession;
    this.simpleGrantedAuthorityMapper = simpleGrantedAuthorityMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity user = userTransactionSession.getUserInformationByEmail(email);

    if(user == null) throw new UsernameNotFoundException("email ou mot de passe invalide");

    return UserPrincipal.builder()
            .withId(user.getId())
            .withEmail(user.getEmail())
            .withPassword(user.getPassword())
            .withIsAccountActive(true)
            .withAuthorities(simpleGrantedAuthorityMapper.apply(user.getRoles()))
            .build();
  }
}
