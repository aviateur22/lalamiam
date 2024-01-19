package ctoutweb.lalamiam.repository.builder;

import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;

public final class CommandProductEntityBuilder {
  private BigInteger id;
  private Integer productQuantity;
  private CommandEntity command;
  private ProductEntity product;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private CommandProductEntityBuilder() {
  }

  public static CommandProductEntityBuilder aCommandProductEntity() {
    return new CommandProductEntityBuilder();
  }

  public CommandProductEntityBuilder withId(BigInteger id) {
    this.id = id;
    return this;
  }

  public CommandProductEntityBuilder withProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
    return this;
  }

  public CommandProductEntityBuilder withCommand(CommandEntity command) {
    this.command = command;
    return this;
  }

  public CommandProductEntityBuilder withProduct(ProductEntity product) {
    this.product = product;
    return this;
  }

  public CommandProductEntityBuilder withCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public CommandProductEntityBuilder withUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public CommandProductEntity build() {
    CommandProductEntity commandProductEntity = new CommandProductEntity();
    commandProductEntity.setId(id);
    commandProductEntity.setProductQuantity(productQuantity);
    commandProductEntity.setCommand(command);
    commandProductEntity.setProduct(product);
    commandProductEntity.setCreatedAt(createdAt);
    commandProductEntity.setUpdatedAt(updatedAt);
    return commandProductEntity;
  }
}
