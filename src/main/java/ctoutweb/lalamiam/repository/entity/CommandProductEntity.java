package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "command_product")
public class CommandProductEntity {
  @Id
  @SequenceGenerator(name="command_product_pk_seq", sequenceName="command_product_pk_seq", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "command_product_pk_seq")
  private BigInteger id;

  @Column(name = "product_quantity")
  private Integer productQuantity;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "command_id")
  CommandEntity command;


  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "product_id")
  ProductEntity product;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public CommandProductEntity() {
  }

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public CommandEntity getCommand() {
    return command;
  }

  public void setCommand(CommandEntity command) {
    this.command = command;
  }
  @JsonBackReference
  public ProductEntity getProduct() {
    return product;
  }

  public void setProduct(ProductEntity product) {
    this.product = product;
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

  public Integer getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandProductEntity that = (CommandProductEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(productQuantity, that.productQuantity) && Objects.equals(command, that.command) && Objects.equals(product, that.product) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
  }

//  @Override
//  public int hashCode() {
//    return Objects.hash(id, productQuantity, command, store, product, createdAt, updatedAt);
//  }

  @Override
  public String toString() {
    return "CookEntity{" +
            "id=" + id +
            ", productQuantity=" + productQuantity +
            //", command=" + command +
            //", product=" + product +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
