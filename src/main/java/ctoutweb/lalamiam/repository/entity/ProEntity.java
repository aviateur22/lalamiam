package ctoutweb.lalamiam.repository.entity;

import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pro")
public class ProEntity {
  @Id
  @SequenceGenerator(name="proPkSeq", sequenceName="PRO_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proPkSeq")
  private BigInteger id;

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

  public ProEntity(BigInteger id, String email, String phone, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public ProEntity(AddProfessionalSchema addProfessionalSchema) {
    this.email = addProfessionalSchema.email();
    this.phone = addProfessionalSchema.phone();
    this.password = addProfessionalSchema.password();
  }

  public ProEntity(BigInteger proId) {
    this.id = proId;
  }

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
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

  public Set<StoreEntity> getStores() {
    return stores;
  }

  public void setStores(Set<StoreEntity> stores) {
    this.stores = stores;
  }
}
