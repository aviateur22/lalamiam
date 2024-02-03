package helper;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class CommandHelper {
  /**
   * Génération d'une date a partir d'un jour de semaine
   * @param targetWeekDayNumber - Integer Jour de semaine à produire entre 1 et 7
   * @param dayReference - DayReference - Précise si la date doit être dans le passé future present
   * @return LocalDateTime
   */
  public LocalDateTime getDateOfDay(Integer targetWeekDayNumber, DayReference dayReference) {
    switch (dayReference) {
      case TODAY -> {
        return findNextDay(LocalDateTime.now().plusDays(1), targetWeekDayNumber);
      }
      case TOMORROW -> {
        return findNextDay(LocalDateTime.now().plusDays(2), targetWeekDayNumber);
      }
      case YESTERDAY -> {
        return findNextDay(LocalDateTime.now(), targetWeekDayNumber);
      }
      default -> throw new RuntimeException("Erreur sur la recherche d'une date");
    }
  }

  /**
   * Trouve la prochaine occurence d'une date recherchée
   * @param refDate - LocalDateTime - Date de reférence
   * @param targetWeekDayNumber - Integer jour de semaine recherché
   * @return LocalDateTime
   */
  private LocalDateTime findNextDay(LocalDateTime refDate, Integer targetWeekDayNumber) {
    return Stream.iterate(refDate, date-> date.plusDays(1))
            .limit(7)
            .filter(date-> date.getDayOfWeek().getValue() == targetWeekDayNumber)
            .findFirst()
            .orElse(null);
  }
}
