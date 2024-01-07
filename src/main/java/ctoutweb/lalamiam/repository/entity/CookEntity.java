package ctoutweb.lalamiam.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "cook")
public class CookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private BigInteger id;

  @Column(name = "command_id")
  private BigInteger commandId;

  @Column(name = "product_id")
  private BigInteger productId;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;







}
