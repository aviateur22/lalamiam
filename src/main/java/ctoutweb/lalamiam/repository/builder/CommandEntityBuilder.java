package ctoutweb.lalamiam.repository.builder;

import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

public final class CommandEntityBuilder {
  private BigInteger id;
  private String clientPhone;
  private Integer preparationTime;
  private Double orderPrice;
  private LocalDateTime slotTime;
  private String commandCode;
  private Integer productQuantity;
  private Set<CommandProductEntity> commandProducts;
  private StoreEntity store;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private CommandEntityBuilder() {
  }

  public static CommandEntityBuilder aCommandEntity() {
    return new CommandEntityBuilder();
  }

  public CommandEntityBuilder withId(BigInteger id) {
    this.id = id;
    return this;
  }

  public CommandEntityBuilder withClientPhone(String clientPhone) {
    this.clientPhone = clientPhone;
    return this;
  }

  public CommandEntityBuilder withPreparationTime(Integer preparationTime) {
    this.preparationTime = preparationTime;
    return this;
  }

  public CommandEntityBuilder withOrderPrice(Double orderPrice) {
    this.orderPrice = orderPrice;
    return this;
  }

  public CommandEntityBuilder withSlotTime(LocalDateTime slotTime) {
    this.slotTime = slotTime;
    return this;
  }

  public CommandEntityBuilder withCommandCode(String commandCode) {
    this.commandCode = commandCode;
    return this;
  }

  public CommandEntityBuilder withProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
    return this;
  }

  public CommandEntityBuilder withCommandProducts(Set<CommandProductEntity> commandProducts) {
    this.commandProducts = commandProducts;
    return this;
  }

  public CommandEntityBuilder withStore(StoreEntity store) {
    this.store = store;
    return this;
  }

  public CommandEntityBuilder withCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public CommandEntityBuilder withUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public CommandEntity build() {
    CommandEntity commandEntity = new CommandEntity();
    commandEntity.setId(id);
    commandEntity.setClientPhone(clientPhone);
    commandEntity.setPreparationTime(preparationTime);
    commandEntity.setCommandPrice(orderPrice);
    commandEntity.setSlotTime(slotTime);
    commandEntity.setCommandCode(commandCode);
    commandEntity.setProductQuantity(productQuantity);
    commandEntity.setCommandProducts(commandProducts);
    commandEntity.setStore(store);
    commandEntity.setCreatedAt(createdAt);
    commandEntity.setUpdatedAt(updatedAt);
    return commandEntity;
  }
}
