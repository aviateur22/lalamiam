package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "command")
public class CommandEntity {
  @Id
  @SequenceGenerator(name="commandPkSeq", sequenceName="COMMAND_PK_SEQ", allocationSize=1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commandPkSeq")
  private BigInteger id;

  @Column(name = "client_phone")
  private String clientPhone;

  @Column(name = "preparation_time")
  private Integer preparationTime;

  @Column(name = "order_price")
  private Double orderPrice;

  @Column(name = "slot_time")
  private LocalDateTime slotTime;

  @Column(name = "command_code")
  private String commandCode;

  @JsonIgnore
  @OneToMany(mappedBy = "command", fetch = FetchType.LAZY)
  Set<CookEntity> cooks;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  /**
   *
   */

  public CommandEntity() {
  }

  public CommandEntity(BigInteger commandId) {
    this.id = commandId;
  }

  public CommandEntity(AddCommandSchema addCommandSchema) {
    this.slotTime = addCommandSchema.slotTime();
    this.clientPhone = addCommandSchema.clientPhone();
  }
  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
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

  public Double getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(Double orderPrice) {
    this.orderPrice = orderPrice;
  }

  public LocalDateTime getSlotTime() {
    return slotTime;
  }

  public void setSlotTime(LocalDateTime slotTime) {
    this.slotTime = slotTime;
  }

  public Set<CookEntity> getCooks() {
    return cooks;
  }

  public void setCooks(Set<CookEntity> cooks) {
    this.cooks = cooks;
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

  public String getCommandCode() {
    return commandCode;
  }

  public void setCommandCode(String commandCode) {
    this.commandCode = commandCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandEntity that = (CommandEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(clientPhone, that.clientPhone) && Objects.equals(preparationTime, that.preparationTime) && Objects.equals(orderPrice, that.orderPrice) && Objects.equals(slotTime, that.slotTime) && Objects.equals(cooks, that.cooks) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientPhone, preparationTime, orderPrice, slotTime, cooks, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "CommandEntity{" +
            "id=" + id +
            ", clientPhone='" + clientPhone + '\'' +
            ", preparationTime=" + preparationTime +
            ", orderPrice=" + orderPrice +
            ", slotTime=" + slotTime +
            ", cooks=" + cooks +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
