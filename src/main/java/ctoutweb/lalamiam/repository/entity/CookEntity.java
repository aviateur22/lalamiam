package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ctoutweb.lalamiam.model.schema.AddCookSchema;
import ctoutweb.lalamiam.model.schema.DeleteProductInCommandSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductQuantityInCommandSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cook")
public class CookEntity {
  @Id
  @SequenceGenerator(name="cookPkSeq", sequenceName="COOK_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cookPkSeq")
  private BigInteger id;

  @Column(name = "product_quantity")
  private Integer productQuantity;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "command_id")
  CommandEntity command;

  @ManyToOne
  @JoinColumn(name = "store_id")
  StoreEntity store;

  @ManyToOne
  @JoinColumn(name = "product_id")
  ProductEntity product;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   *
   */
  public CookEntity() {
  }

  public CookEntity(AddCookSchema addCookSchema) {
    this.command = new CommandEntity(addCookSchema.commandId());
    this.store = new StoreEntity(addCookSchema.storeId());
    this.product = new ProductEntity(addCookSchema.productId());
    this.productQuantity = addCookSchema.productQuantity();
  }

  public CookEntity(DeleteProductInCommandSchema deleteProductInCommand) {
    this.command = new CommandEntity(deleteProductInCommand.commandId());
    this.store = new StoreEntity(deleteProductInCommand.storeId());
    this.product = new ProductEntity(deleteProductInCommand.productId());
  }

  public CookEntity(UpdateProductQuantityInCommandSchema updateProductCommand) {
    this.command = new CommandEntity(updateProductCommand.getCommandId());
    this.store = new StoreEntity(updateProductCommand.getStoreId());
    this.product = new ProductEntity(updateProductCommand.getProductId());
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
  public StoreEntity getStore() {
    return store;
  }

  public void setStore(StoreEntity store) {
    this.store = store;
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
    CookEntity that = (CookEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(productQuantity, that.productQuantity) && Objects.equals(command, that.command) && Objects.equals(store, that.store) && Objects.equals(product, that.product) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
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
            ", store=" + store +
            ", product=" + product +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
