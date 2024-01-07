package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "store")
public class StoreEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private BigInteger id;
  @Column(name = "pro_id")
  private BigInteger proId;

  @Column(name = "name")
  private String name;

  @Column(name = "presentation")
  private String presentation;

  @Column(name = "adress")
  private String adress;

  @Column(name = "city")
  private String city;

  @Column(name = "cp")
  private String cp;
  @Column(name = "phone")
  private String phone;

  @Column(name = "photo")
  private String photo;
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
