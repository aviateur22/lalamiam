package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pro")
public class ProEntity {
  @Id
  @SequenceGenerator(name="proPkSeq", sequenceName="PRO_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proPkSeq")
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "password")
  private String password;

  @Generated(GenerationTime.INSERT)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "pro")
  private Set<StoreEntity> stores;

  /**
   *
   */
  public ProEntity() {
  }

  public ProEntity(Long id, String email, String phone, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public ProEntity(AddProfessionalDto addProfessionalSchema) {
    this.email = addProfessionalSchema.email();
    this.phone = addProfessionalSchema.phone();
    this.password = addProfessionalSchema.password();
  }

  public ProEntity(Long proId) {
    this.id = proId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @JsonManagedReference
  public Set<StoreEntity> getStores() {
    return stores;
  }

  public void setStores(Set<StoreEntity> stores) {
    this.stores = stores;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProEntity proEntity = (ProEntity) o;
    return Objects.equals(id, proEntity.id) && Objects.equals(email, proEntity.email) && Objects.equals(phone, proEntity.phone) && Objects.equals(password, proEntity.password) && Objects.equals(createdAt, proEntity.createdAt) && Objects.equals(updatedAt, proEntity.updatedAt) && Objects.equals(stores, proEntity.stores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, phone, password, createdAt, updatedAt, stores);
  }

  @Override
  public String toString() {
    return "ProEntity{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", password='" + password + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            //", stores=" + stores +
            '}';
  }
}
