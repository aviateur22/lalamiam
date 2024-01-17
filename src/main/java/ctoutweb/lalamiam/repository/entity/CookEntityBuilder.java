package ctoutweb.lalamiam.repository.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

public final class CookEntityBuilder {
  private BigInteger id;
  private Integer productQuantity;
  private CommandEntity command;
  private StoreEntity store;
  private ProductEntity product;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private CookEntityBuilder() {
  }

  public static CookEntityBuilder aCookEntity() {
    return new CookEntityBuilder();
  }

  public CookEntityBuilder withId(BigInteger id) {
    this.id = id;
    return this;
  }

  public CookEntityBuilder withProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
    return this;
  }

  public CookEntityBuilder withCommand(CommandEntity command) {
    this.command = command;
    return this;
  }

  public CookEntityBuilder withStore(StoreEntity store) {
    this.store = store;
    return this;
  }

  public CookEntityBuilder withProduct(ProductEntity product) {
    this.product = product;
    return this;
  }

  public CookEntityBuilder withCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public CookEntityBuilder withUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public CookEntity build() {
    CookEntity cookEntity = new CookEntity();
    cookEntity.setId(id);
    cookEntity.setProductQuantity(productQuantity);
    cookEntity.setCommand(command);
    cookEntity.setStore(store);
    cookEntity.setProduct(product);
    cookEntity.setCreatedAt(createdAt);
    cookEntity.setUpdatedAt(updatedAt);
    return cookEntity;
  }
}
