package ctoutweb.lalamiam.repository.entity.builder;

import ctoutweb.lalamiam.repository.entity.JwtUserEntity;
import ctoutweb.lalamiam.repository.entity.UserEntity;

import java.time.LocalDateTime;

public final class JwtUserEntityBuilder {
  private Long id;

  private String jwtId;
  private String jwtToken;
  private Boolean isValid;
  private LocalDateTime expiredAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UserEntity user;

  private JwtUserEntityBuilder() {
  }

  public static JwtUserEntityBuilder aJwtUserEntity() {
    return new JwtUserEntityBuilder();
  }

  public JwtUserEntityBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public JwtUserEntityBuilder withIsValid(Boolean isValid) {
    this.isValid = isValid;
    return this;
  }
  public JwtUserEntityBuilder withJwtId(String jwtId) {
    this.jwtId = jwtId;
    return this;
  }


  public JwtUserEntityBuilder withJwtToken(String jwt) {
    this.jwtToken = jwt;
    return this;
  }

  public JwtUserEntityBuilder withExpiredAt(LocalDateTime expiredAt) {
    this.expiredAt = expiredAt;
    return this;
  }

  public JwtUserEntityBuilder withCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public JwtUserEntityBuilder withUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public JwtUserEntityBuilder withUser(UserEntity user) {
    this.user = user;
    return this;
  }

  public JwtUserEntity build() {
    JwtUserEntity jwtUserEntity = new JwtUserEntity();
    jwtUserEntity.setId(id);
    jwtUserEntity.setIsValid(isValid);
    jwtUserEntity.setJwtId(jwtId);
    jwtUserEntity.setJwtToken(jwtToken);
    jwtUserEntity.setExpiredAt(expiredAt);
    jwtUserEntity.setCreatedAt(createdAt);
    jwtUserEntity.setUpdatedAt(updatedAt);
    jwtUserEntity.setUser(user);
    return jwtUserEntity;
  }
}
