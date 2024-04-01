package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_user")
public class JwtUserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "jwt_id")
  private String jwtId;

  @Column(name = "jwt_token")
  private String jwtToken;
  @Column(name = "is_valid")
  private Boolean isValid;

  @Column(name = "expired_at")
  private LocalDateTime expiredAt;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  ///////////////////////////////////////////////////////////////////////////////////////////


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwt) {
    this.jwtToken = jwt;
  }

  public Boolean getIsValid() {
    return isValid;
  }

  public void setIsValid(Boolean valid) {
    isValid = valid;
  }

  public LocalDateTime getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(LocalDateTime expiredAt) {
    this.expiredAt = expiredAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public String getJwtId() {
    return jwtId;
  }

  public void setJwtId(String jwtId) {
    this.jwtId = jwtId;
  }

  @Override
  public String toString() {
    return "JwtUserEntity{" +
            "id=" + id +
            "jwtId='" + jwtId + '\'' +
            ", jwt='" + jwtToken + '\'' +
            ", isValid=" + isValid +
            ", expiredAt=" + expiredAt +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", user=" + user +
            '}';
  }
}
