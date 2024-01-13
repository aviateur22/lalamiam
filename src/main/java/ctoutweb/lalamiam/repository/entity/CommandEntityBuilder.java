package ctoutweb.lalamiam.repository.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

public final class CommandEntityBuilder {
  private BigInteger id;
  private String clientPhone;
  private Integer preparationTime;
  private Double orderPrice;
  private LocalDateTime slotTime;
  private Set<CookEntity> cooks;
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

  public CommandEntityBuilder withCooks(Set<CookEntity> cooks) {
    this.cooks = cooks;
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
    commandEntity.setOrderPrice(orderPrice);
    commandEntity.setSlotTime(slotTime);
    commandEntity.setCooks(cooks);
    commandEntity.setCreatedAt(createdAt);
    commandEntity.setUpdatedAt(updatedAt);
    return commandEntity;
  }
}
