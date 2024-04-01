package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.repository.entity.RoleEntity;
import ctoutweb.lalamiam.repository.entity.RoleUserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Convertion Roles en SimpleGrantedAuthority
 */
@Component
public class SimpleGrantedAuthorityMapper implements Function<List<RoleUserEntity> , List<SimpleGrantedAuthority>> {

  @Override
  public List<SimpleGrantedAuthority> apply(List<RoleUserEntity> roles) {
    return roles
            .stream()
            .map(role->role.getRole())
            .map(role->new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
  }
}
