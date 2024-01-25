package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class FindListOfSlotTimeAvailableDto {
  private LocalDate commandDate;
  private BigInteger StoreId;
  private Integer commandPreparationTime;
  private LocalDateTime SlotConslutationDate;

  ////////////////////////////////////////////////////////


  public FindListOfSlotTimeAvailableDto() {
  }

  public FindListOfSlotTimeAvailableDto(
          LocalDate commandRequestedDate,
          BigInteger storeId,
          Integer commandPreparationTime,
          LocalDateTime slotConslutationDate
  ) {
    this.commandDate = commandRequestedDate;
    StoreId = storeId;
    this.commandPreparationTime = commandPreparationTime;
    SlotConslutationDate = slotConslutationDate;
  }

  public LocalDate getCommandDate() {
    return commandDate;
  }

  public void setCommandDate(LocalDate commandDate) {
    this.commandDate = commandDate;
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

  public LocalDateTime getSlotConslutationDate() {
    return SlotConslutationDate;
  }

  public void setSlotConslutationDate(LocalDateTime slotConslutationDate) {
    SlotConslutationDate = slotConslutationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FindListOfSlotTimeAvailableDto that = (FindListOfSlotTimeAvailableDto) o;
    return Objects.equals(commandDate, that.commandDate) && Objects.equals(StoreId, that.StoreId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandDate, StoreId);
  }

  @Override
  public String toString() {
    return "FindSlotTimeDto{" +
            "commandRequestedDate=" + commandDate +
            ", StoreId=" + StoreId +
            '}';
  }
}
