package ctoutweb.lalamiam.test.helper;

import ctoutweb.lalamiam.model.DailyStoreSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CommonFunction {
  public static LocalDateTime getLocalDateTime(LocalDate dateTime, LocalTime time) {
    LocalDateTime calculateDateTime = LocalDateTime.of(
            dateTime.getYear(),
            dateTime.getMonth(),
            dateTime.getDayOfMonth(),
            time.getHour(),
            time.getMinute(),
            time.getSecond());
    return calculateDateTime;
  }

  public static boolean isSlotInStoreSchedule(LocalDateTime slot, LocalDateTime consultationDate, DailyStoreSchedule schedule, int preparationTime) {
    boolean isInSchedule =
            (slot.isAfter(getLocalDateTime(consultationDate.toLocalDate(),schedule.getOpeningTime()).plusMinutes(preparationTime))
            || slot.isEqual(getLocalDateTime(consultationDate.toLocalDate(), schedule.getOpeningTime()).plusMinutes(preparationTime)))
            && slot.isBefore(getLocalDateTime(consultationDate.toLocalDate(), schedule.getClosingTime()));

    return isInSchedule;
  }
}
