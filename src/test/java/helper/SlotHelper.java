package helper;

import ctoutweb.lalamiam.model.DailyStoreSchedule;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.test.helper.CommonFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlotHelper {


  private final CommandRepository commandRepository;

  public SlotHelper(CommandRepository commandRepository) {
    this.commandRepository = commandRepository;
  }

  /**
   * Calcul du nombre de slot disponible pour une commande
   * @param store
   * @param preparationTime
   * @param consultationDate
   * @return long
   */
  public List<LocalDateTime> findSlotAvail(
          List<DailyStoreSchedule> dailyStoreSchedules,
          StoreEntity store,
          int preparationTime,
          LocalDateTime consultationDate
  ) {

    // Liste des slot disponible
    List<LocalDateTime> slotsAvailibilityList = getSlotsAvailibilityInOneDay(store.getFrequenceSlotTime(), consultationDate.toLocalDate());

    // Slot manquant suite temps de préparation
    slotsAvailibilityList = filterSlotByStoreSchedule(
            dailyStoreSchedules,
            preparationTime,
            consultationDate,
            slotsAvailibilityList
    );

    // slot manquant suite à l'horaire d'observation
    slotsAvailibilityList = filterSlotByConsultationDate(
            preparationTime,
            consultationDate,
            slotsAvailibilityList);

    // Filtre les slots déja occupé
    slotsAvailibilityList = filterBusySlot(
            consultationDate,
            LocalDateTime.of(
                    consultationDate.getYear(),
                    consultationDate.getMonth(),
                    consultationDate.getDayOfMonth(),
                    23,
                    59,
                    59
            ),
            store,
            slotsAvailibilityList);



    return slotsAvailibilityList;
  }

  private List<LocalDateTime> getSlotsAvailibilityInOneDay(Integer frequenceSlotTime, LocalDate commandDate) {
    int slotInOneDay = (int) Math.floor(24 * 60 / (float) frequenceSlotTime);
    LocalDateTime begin = LocalDateTime.of(
            commandDate.getYear(),
            commandDate.getMonth(),
            commandDate.getDayOfMonth(),
            0,
            0,
            0);

    LocalDateTime end = LocalDateTime.of(
            commandDate.getYear(),
            commandDate.getMonth(),
            commandDate.getDayOfMonth(),
            23,
            59,
            0);
    return Stream
            .iterate(begin, action -> action.isBefore(end), action -> action.plusMinutes(frequenceSlotTime))
            .collect(Collectors.toList());
  }

  /**
   * Nombre de slot manquant du au temps de préparation
   * @param dailyStoreSchedules
   * @param preparationTime
   * @param consultationDate
   * @return List<LocalDateTime>
   */
  public List<LocalDateTime> filterSlotByStoreSchedule(
          List<DailyStoreSchedule> dailyStoreSchedules,
          int preparationTime,
          LocalDateTime consultationDate,
          List<LocalDateTime> slotAvailibilityList) {

    List<LocalDateTime> filterSlotAvailibility = slotAvailibilityList.stream().filter(
                    slot->{
                      return dailyStoreSchedules
                              .stream()
                              .anyMatch(schedule-> CommonFunction.isSlotInStoreSchedule(slot, consultationDate, schedule, preparationTime));
                    })
            .collect(Collectors.toList());

    return filterSlotAvailibility;
  }

  /**
   * Nombre de Slot manquant due à l'heure d'observation
   * @param consultationDate
   * @param  preparationTime
   * @return List<LocalDateTime>
   */
  public List<LocalDateTime> filterSlotByConsultationDate(
          int preparationTime,
          LocalDateTime consultationDate,
          List<LocalDateTime> slotAvailibilityList) {

    List<LocalDateTime> filterSlotAvailibility = slotAvailibilityList
            .stream()
            .filter(
                    slot->slot.isAfter(consultationDate.plusMinutes(preparationTime))
            ).collect(Collectors.toList());

    return  filterSlotAvailibility;
  }

  /**
   * Nombre de slot occupés par une commande
   * @return
   */
  public List<LocalDateTime> filterBusySlot(LocalDateTime beginDay, LocalDateTime endDay, StoreEntity store, List<LocalDateTime> slotAvailibilityList) {

    List<LocalDateTime> commandsSlotTime = commandRepository
            .findCommandsByStoreIdDate(beginDay, endDay, store.getId())
            .stream()
            .map(CommandEntity::getSlotTime)
            .collect(Collectors.toList());
    List<LocalDateTime> filterSlotAvailibility = slotAvailibilityList
            .stream()
            .filter(slot->!commandsSlotTime.contains(slot))
            .collect(Collectors.toList());
    return filterSlotAvailibility;
  }
}
