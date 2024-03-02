package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
import ctoutweb.lalamiam.service.StoreService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recherche des slots de disponible
 */
@Component
public class NewSlotHelper {
  private final StoreService storeService;
  private LocalDateTime startOfCommandDay;
  private LocalDateTime endOfCommandDay;
  private Integer commandPreparationTime;
  private LocalDateTime refFilterTime;

  public NewSlotHelper(StoreService storeService) {
    this.storeService = storeService;
  }

  /**
   * Recherche des slots disponible pour une commande
   * @param storeId BigInteger - Identifiant commerce
   * @return List<LocalDateTime> - Liste des slots disponible pour la commande
   */
  public List<LocalDateTime> getStoreSlotAvailibility(Long storeId, List<CommandEntity> storeCommandsInDay) {

    // Recherche Info Commerce
    StoreEntity store = storeService.findStoreById(storeId);

    // Rechechre des slots déja pris
    List<LocalDateTime> busySlots  = storeCommandsInDay.stream().map(CommandEntity::getSlotTime).toList();

    // Recherche du jour de la semaine
    WeekDayEntity weekDay = Factory.getWeekDay(startOfCommandDay);

    // Recherche des horaires du commerce
    List<StoreDayScheduleEntity> storeSchedules = storeService.findStoreSchedulesByDay(store, weekDay);

    // Nombre de Slots disponible sur 24h pour le commerce
    List<LocalDateTime> slotTimeAvailibilityInOneDay = storeService.findStorSlotsWithoutConstraintByDay(startOfCommandDay, store.getFrequenceSlotTime());

    // Recherhe de slot disponible
    return filterSlots(
      slotTimeAvailibilityInOneDay,
      storeSchedules,
      busySlots);
  }

  /**
   * Filtre les slots qui sont disponible pour la commande
   * @param slotTimeAvailibilityInOneDay List<LocalDateTime> - Liste des slots disponible sur 24h
   * @param storeSchedules - List<StoreDayScheduleEntity> - Horaires du commerce sur la journée de la commande
   * @param busySlots List<LocalDateTime> - Liste des slot déja occupés
   * @return List<LocalDateTime>
   */
  public List<LocalDateTime> filterSlots(
          List<LocalDateTime> slotTimeAvailibilityInOneDay,
          List<StoreDayScheduleEntity> storeSchedules,
          List<LocalDateTime> busySlots
  ) {

    return slotTimeAvailibilityInOneDay
            .stream()
            // Retir les slots avant REF_FILTER_TIME
            .filter(slot->slot.isAfter(refFilterTime.plusMinutes(commandPreparationTime)))

            // Retire les slots déja pris par d'autres commandes
            .filter(slot->!busySlots.contains(slot))

            // Retire tous les slots qui ne sont pas dans les horaires du commerce
            .filter(slot->storeSchedules
                    .stream()
                    .anyMatch(schedule-> CommonFunction.isSlotInStoreSchedule(slot,
                            schedule,
                            startOfCommandDay.toLocalDate(),
                            endOfCommandDay.toLocalDate(),
                            commandPreparationTime
                    )))
            .collect(Collectors.toList());
  }

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  public LocalDateTime getStartOfCommandDay() {
    return startOfCommandDay;
  }
  public void setStartOfCommandDay(LocalDateTime startOfCommandDay) {
    this.startOfCommandDay = startOfCommandDay;
  }
  public LocalDateTime getEndOfCommandDay() {
    return endOfCommandDay;
  }
  public void setEndOfCommandDay(LocalDateTime endOfCommandDay) {
    this.endOfCommandDay = endOfCommandDay;
  }

  public Integer getCommandPreparationTime() {
    return commandPreparationTime;
  }

  public void setCommandPreparationTime(Integer commandPreparationTime) {
    this.commandPreparationTime = commandPreparationTime;
  }
  public LocalDateTime getRefFilterTime() {
    return refFilterTime;
  }
  public void setRefFilterTime(LocalDateTime refFilterTime) {
    this.refFilterTime = refFilterTime;
  }
}
