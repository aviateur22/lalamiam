package ctoutweb.lalamiam.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class UserPrincipal implements UserDetails {
  private Long id;
  private String email;
  private String password;
  private Collection< ? extends GrantedAuthority > authorities;
  private Boolean isAccountActive;

  private UserPrincipal(Builder builder) {
    this.id = builder.id;
    this.email = builder.email;
    this.password = builder.password;
    this.authorities = builder.authorities;
    this.isAccountActive = builder.isAccountActive;
  }

  public Long getId() {
    return id;
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isAccountActive;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
    private boolean isAccountActive;
    private Builder() {
    }

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder withPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder withAuthorities(Collection<? extends GrantedAuthority> authorities) {
      this.authorities = authorities;
      return this;
    }

    public Builder withIsAccountActive(boolean isAccountActive) {
      this.isAccountActive = isAccountActive;
      return this;
    }

    public Builder withMaximumAccountActivationDate(Date maximumAccountActivationDate) {
      return this;
    }

    public UserPrincipal build() {
      return new UserPrincipal(this);
    }
  }
}
