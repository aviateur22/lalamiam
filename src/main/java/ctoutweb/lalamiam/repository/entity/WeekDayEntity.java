package ctoutweb.lalamiam.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "week_day")
public class WeekDayEntity {
  @Id
  private Integer id;

  @Column(name = "day_text")
  private String dayText;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @JsonIgnore
  @OneToMany(mappedBy = "weekDay", fetch = FetchType.LAZY)
  private List<StoreDayScheduleEntity> StoreWeekDays;


  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public WeekDayEntity() {
  }

  public WeekDayEntity(Integer dayId) {
    this.id = dayId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDayText() {
    return dayText;
  }

  public void setDayText(String dayText) {
    this.dayText = dayText;
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

  public List<StoreDayScheduleEntity> getStoreWeekDays() {
    return StoreWeekDays;
  }

  public void setStoreWeekDays(List<StoreDayScheduleEntity> storeWeekDays) {
    StoreWeekDays = storeWeekDays;
  }
}
