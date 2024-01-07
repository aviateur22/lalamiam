package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "command")
public class CommandEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private BigInteger id;

  @Column(name = "client_phone")
  private String clientPhone;

  @Column(name = "preparation_time")
  private Integer preparationTime;

  @Column(name = "price")
  private float price;

  @Column(name = "slot_time")
  private LocalDateTime slotTime;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
