package ctoutweb.lalamiam.repository.entity;

import ctoutweb.lalamiam.model.schema.AddCookSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductCommandSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "cook")
public class CookEntity {
  @Id
  @SequenceGenerator(name="cookPkSeq", sequenceName="COOK_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cookPkSeq")
  private BigInteger id;

  @Column(name = "product_quantity")
  private Integer productQuantity;

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

  public CookEntity(UpdateProductCommandSchema updateProductCommand) {
    this.command = new CommandEntity(updateProductCommand.commandId());
    this.store = new StoreEntity(updateProductCommand.storeId());
    this.product = new ProductEntity(updateProductCommand.productId());
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

  public StoreEntity getStore() {
    return store;
  }

  public void setStore(StoreEntity store) {
    this.store = store;
  }

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
}
