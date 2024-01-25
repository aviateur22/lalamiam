package ctoutweb.lalamiam.model;

import java.time.LocalTime;
import java.util.Objects;

public class StoreSchedule {
  LocalTime openingTime;
  LocalTime closingTime;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////


  public StoreSchedule(LocalTime openingTime, LocalTime closingTime) {
    this.openingTime = openingTime;
    this.closingTime = closingTime;
  }

  public StoreSchedule() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StoreSchedule that = (StoreSchedule) o;
    return Objects.equals(openingTime, that.openingTime) && Objects.equals(closingTime, that.closingTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openingTime, closingTime);
  }

  @Override
  public String toString() {
    return "StoreSchedule{" +
            "opening_time=" + openingTime +
            ", closing_time=" + closingTime +
            '}';
  }
}
