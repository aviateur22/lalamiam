package ctoutweb.lalamiam.util;

import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CommonFunction {
  public static boolean isNullOrEmpty(String word) {
    if(word == null || word.isEmpty() || word.isBlank()) return true;
    return false;
  }

  public static boolean isNumber(String numberText) {
    try
    {
      Integer.parseInt(numberText);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }

  /**
   * Renvoie une date a partir d'un localDate et LocalTime
   * @param time
   * @param date
   * @return LocalDateTime
   */
  public static LocalDateTime getLocalDateTime(LocalTime time, LocalDate date) {
    return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond());
  }

  /**
   * Vérifie si une date est dans une perdiode de temps
   * @param slot
   * @param schedule
   * @param startingDay
   * @param endingDay
   * @param commandPreparationTime
   * @return boolean
   */
  public static boolean isSlotInStoreSchedule(
          LocalDateTime slot,
          StoreDayScheduleEntity schedule,
          LocalDate startingDay,
          LocalDate endingDay,
          int commandPreparationTime
  ) {
    return (
            slot.isEqual(CommonFunction.getLocalDateTime(schedule.getOpeningTime(), startingDay)
                    .plusMinutes(commandPreparationTime)) ||
            slot.isAfter(CommonFunction.getLocalDateTime(schedule.getOpeningTime(), startingDay)
                    .plusMinutes(commandPreparationTime)) &&
            slot.isBefore(CommonFunction.getLocalDateTime(schedule.getClosingTime(), endingDay))
    );
  }
}
