package ctoutweb.lalamiam.repository.entity;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.CommandInformationToSave;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "command")
public class CommandEntity implements Serializable {
  @Id
  @SequenceGenerator(name="commandPkSeq", sequenceName="COMMAND_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commandPkSeq")
  private Long id;

  @Column(name = "client_phone")
  private String clientPhone;

  @Column(name = "preparation_time")
  private Integer preparationTime;

  @Column(name = "order_price")
  private Double commandPrice;

  @Column(name = "slot_time")
  private LocalDateTime slotTime;

  @Column(name = "command_code")
  private String commandCode;

  @Column(name = "product_quantity")
  private Integer productQuantity;

  @OneToMany(mappedBy = "command", fetch = FetchType.LAZY)
  private List<CommandProductEntity> commandProducts;

  @ManyToOne
  @JoinColumn(name="store_id", nullable=false)
  private StoreEntity store;

  @Column(name = "is_ready")
  private Boolean isReady;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /////////////////////////////////////////////////////////////////////////////////////////

  public CommandEntity() {
  }

  public CommandEntity(Long commandId) {
    this.id = commandId;
  }

  public CommandEntity(
          Long id,
          String clientPhone,
          Integer preparationTime,
          Double commandPrice,
          LocalDateTime slotTime,
          String commandCode,
          Integer productQuantity,
          List<CommandProductEntity> commandProducts,
          StoreEntity store,
          LocalDateTime updatedAt
  ) {
    this.id = id;
    this.clientPhone = clientPhone;
    this.commandCode = commandCode;
    this.preparationTime = preparationTime;
    this.commandPrice = commandPrice;
    this.slotTime = slotTime;
    this.productQuantity = productQuantity;
    this.commandProducts = commandProducts;
    this.store = store;
    this.updatedAt = updatedAt;
  }

  public CommandEntity(CommandInformationToSave commandInformationToSave) {
    this.store = Factory.getStore(commandInformationToSave.storeId());
    this.clientPhone = commandInformationToSave.clientPhone();
    this.commandCode = commandInformationToSave.commandCode();
    this.preparationTime = commandInformationToSave.preparationTime();
    this.commandPrice = commandInformationToSave.commandPrice();
    this.slotTime = commandInformationToSave.slotTime();
    this.isReady = false;
    this.productQuantity = commandInformationToSave.numberOfProductInCommand();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClientPhone() {
    return clientPhone;
  }

  public void setClientPhone(String clientPhone) {
    this.clientPhone = clientPhone;
  }

  public Integer getPreparationTime() {
    return preparationTime;
  }

  public void setPreparationTime(Integer preparationTime) {
    this.preparationTime = preparationTime;
  }

  public Double getCommandPrice() {
    return commandPrice;
  }

  public void setCommandPrice(Double commandPrice) {
    this.commandPrice = commandPrice;
  }

  public LocalDateTime getSlotTime() {
    return slotTime;
  }

  public void setSlotTime(LocalDateTime slotTime) {
    this.slotTime = slotTime;
  }

  public String getCommandCode() {
    return commandCode;
  }

  public void setCommandCode(String commandCode) {
    this.commandCode = commandCode;
  }

  public Integer getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Integer productQuantity) {
    this.productQuantity = productQuantity;
  }

  public List<CommandProductEntity> getCommandProducts() {
    return commandProducts;
  }

  public void setCommandProducts(List<CommandProductEntity> commandProducts) {
    this.commandProducts = commandProducts;
  }

  public StoreEntity getStore() {
    return store;
  }

  public void setStore(StoreEntity store) {
    this.store = store;
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

  public Boolean getIsReady() {
    return isReady;
  }

  public void setIsReady(Boolean ready) {
    isReady = ready;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandEntity command = (CommandEntity) o;
    return Objects.equals(id, command.id) && Objects.equals(clientPhone, command.clientPhone) && Objects.equals(preparationTime, command.preparationTime) && Objects.equals(commandPrice, command.commandPrice) && Objects.equals(slotTime, command.slotTime) && Objects.equals(commandCode, command.commandCode) && Objects.equals(productQuantity, command.productQuantity) && Objects.equals(commandProducts, command.commandProducts) && Objects.equals(store, command.store) && Objects.equals(createdAt, command.createdAt) && Objects.equals(updatedAt, command.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientPhone, preparationTime, commandPrice, slotTime, commandCode, productQuantity, commandProducts, store, createdAt, updatedAt);
  }
  @Override
  public String toString() {
    return "CommandEntity{" +
            "id=" + id +
            ", clientPhone='" + clientPhone + '\'' +
            ", preparationTime=" + preparationTime +
            ", orderPrice=" + commandPrice +
            ", slotTime=" + slotTime +
            ", commandCode='" + commandCode + '\'' +
            ", productQuantity=" + productQuantity +
            //", commandProducts=" + commandProducts +
            //", store=" + store +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
