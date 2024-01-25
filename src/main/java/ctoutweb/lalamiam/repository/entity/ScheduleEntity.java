package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.StoreSchedule;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "schedule")
public class ScheduleEntity implements Serializable {
  @Id
  @SequenceGenerator(name = "schedule_pk_seq", sequenceName = "schedule_pk_seq", initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_pk_seq")
  BigInteger id;

  @Column(name = "opening_time")
  LocalTime openingTime;

  @Column(name = "closing_time")
  LocalTime closingTime;

  @Generated(GenerationTime.INSERT)
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "store_id")
  StoreEntity store;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  public ScheduleEntity() {
  }

  public ScheduleEntity(StoreSchedule storeSchedule, BigInteger storeId) {
    this.closingTime = storeSchedule.getClosingTime();
    this.openingTime = storeSchedule.getOpeningTime();
    this.store = Factory.getStore(storeId);
  }

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public LocalTime getOpeningTime() {
    return openingTime;
  }

  public void setOpeningTime(LocalTime openingTime) {
    this.openingTime = openingTime;
  }

  public LocalTime getClosingTime() {
    return closingTime;
  }

  public void setClosingTime(LocalTime closingTime) {
    this.closingTime = closingTime;
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

  public StoreEntity getStore() {
    return store;
  }

  public void setStore(StoreEntity store) {
    this.store = store;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScheduleEntity that = (ScheduleEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(openingTime, that.openingTime) && Objects.equals(closingTime, that.closingTime) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(store, that.store);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, openingTime, closingTime, createdAt, updatedAt, store);
  }

  @Override
  public String toString() {
    return "ScheduleEntity{" +
            "id=" + id +
            ", openingTime=" + openingTime +
            ", closingTime=" + closingTime +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            //", store=" + store +
            '}';
  }
}
