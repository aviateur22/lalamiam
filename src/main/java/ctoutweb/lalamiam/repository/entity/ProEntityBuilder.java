package ctoutweb.lalamiam.repository.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

public final class ProEntityBuilder {
  private BigInteger id;
  private String email;
  private String phone;
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private ProEntityBuilder() {
  }

  public static ProEntityBuilder aProEntity() {
    return new ProEntityBuilder();
  }

  public ProEntityBuilder withId(BigInteger id) {
    this.id = id;
    return this;
  }

  public ProEntityBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public ProEntityBuilder withPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public ProEntityBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public ProEntityBuilder withCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public ProEntityBuilder withUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public ProEntity build() {
    ProEntity proEntity = new ProEntity();
    proEntity.setId(id);
    proEntity.setEmail(email);
    proEntity.setPhone(phone);
    proEntity.setPassword(password);
    proEntity.setCreatedAt(createdAt);
    proEntity.setUpdatedAt(updatedAt);
    return proEntity;
  }
}
