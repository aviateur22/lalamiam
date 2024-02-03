package ctoutweb.lalamiam.repository.entity;

import ctoutweb.lalamiam.model.DailyStoreSchedule;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "store_day_schedule")
public class StoreDayScheduleEntity {
  @Id
  @SequenceGenerator(name = "storeWeekDayPkSeq", sequenceName = "store_week_day_pk_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeWeekDayPkSeq")
  private Integer id;

  @Column(name = "opening_time")
  LocalTime openingTime;

  @Column(name = "closing_time")
  LocalTime closingTime;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name="store_id", nullable=false)
  private StoreEntity store;

  @ManyToOne
  @JoinColumn(name = "week_day_id", nullable = false)
  private WeekDayEntity weekDay;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  public StoreDayScheduleEntity() {
  }

  public StoreDayScheduleEntity(DailyStoreSchedule storeSchedule, WeekDayEntity weekDay, StoreEntity store) {
    this.store = store;
    this.weekDay = weekDay;
    this.openingTime = storeSchedule.getOpeningTime();
    this.closingTime = storeSchedule.getClosingTime();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public WeekDayEntity getWeekDay() {
    return weekDay;
  }

  public void setWeekDay(WeekDayEntity weekDay) {
    this.weekDay = weekDay;
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
}
