package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

public class FindSlotTimeDto {
  private LocalDate commandRequestedDate;
  private BigInteger StoreId;

  private Integer commandPreparationTime;

  ////////////////////////////////////////////////////////


  public FindSlotTimeDto() {
  }

  public FindSlotTimeDto(LocalDate commandRequestedDate, BigInteger storeId, Integer commandPreparationTime) {
    this.commandRequestedDate = commandRequestedDate;
    StoreId = storeId;
    this.commandPreparationTime = commandPreparationTime;
  }

  public LocalDate getCommandRequestedDate() {
    return commandRequestedDate;
  }

  public void setCommandRequestedDate(LocalDate commandRequestedDate) {
    this.commandRequestedDate = commandRequestedDate;
  }

  public BigInteger getStoreId() {
    return StoreId;
  }

  public void setStoreId(BigInteger storeId) {
    StoreId = storeId;
  }

  public Integer getCommandPreparationTime() {
    return commandPreparationTime;
  }

  public void setCommandPreparationTime(Integer commandPreparationTime) {
    this.commandPreparationTime = commandPreparationTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FindSlotTimeDto that = (FindSlotTimeDto) o;
    return Objects.equals(commandRequestedDate, that.commandRequestedDate) && Objects.equals(StoreId, that.StoreId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandRequestedDate, StoreId);
  }

  @Override
  public String toString() {
    return "FindSlotTimeDto{" +
            "commandRequestedDate=" + commandRequestedDate +
            ", StoreId=" + StoreId +
            '}';
  }
}
