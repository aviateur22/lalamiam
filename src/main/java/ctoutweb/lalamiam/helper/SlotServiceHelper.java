//package ctoutweb.lalamiam.helper;
//
//import ctoutweb.lalamiam.factory.Factory;
//import ctoutweb.lalamiam.repository.CommandRepository;
//import ctoutweb.lalamiam.repository.StoreDayScheduleRepository;
//import ctoutweb.lalamiam.repository.entity.CommandEntity;
//import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
//import ctoutweb.lalamiam.repository.entity.StoreEntity;
//import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
//import ctoutweb.lalamiam.util.CommonFunction;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Component
//public class SlotServiceHelper {
//  private final CommandRepository commandRepository;
//  private final StoreDayScheduleRepository storeDayScheduleRepository;
//
//  public SlotServiceHelper(
//          CommandRepository commandRepository,
//          StoreDayScheduleRepository storeDayScheduleRepository
//  ) {
//    this.commandRepository = commandRepository;
//    this.storeDayScheduleRepository = storeDayScheduleRepository;
//  }
//
//  /**
//   *
//   * @param startOfCommandDay LocalDateTime - Début de journée de la commande (00h01)
//   * @param endOfCommandDay LocalDateTime - Fin de la journée de la commande (23h59)
//   * @param refFilterTime LocalDateTime Référence horaire d'affichage
//   * @param commandPreparationTime Integer Temps de préparation
//   * @param store StoreEntity - Commerce Entity
//   * @return List<LocalDateTime>
//   */
//  public List<LocalDateTime> findListOfSlotAvailable(
//          LocalDateTime startOfCommandDay,
//          LocalDateTime endOfCommandDay,
//          LocalDateTime refFilterTime,
//          Integer commandPreparationTime,
//          StoreEntity store
//  ) {
//    // Rechechre des commandes existantes
//    var commands = commandRepository.findCommandsByStoreIdDate(startOfCommandDay, endOfCommandDay, store.getId())
//            .stream().map(CommandEntity::getSlotTime).toList();
//
//    // Recherche du jour de la semaine
//    WeekDayEntity weekDay = Factory.getWeekDay(startOfCommandDay);
//
//    // Recherche des horaires du commerce
//    List<StoreDayScheduleEntity> storeSchedules = storeDayScheduleRepository.findAllByStoreAndWeekDay(store, weekDay);
//
//    // Nombre de Slots disponible sur 24h pour le commerce
//    final Integer ITERATION_PER_DAY = calculateNumberOfCommandSlotForOneDay(store);
//
//    // Recherhe de slot disponible
//    List<LocalDateTime> slotAvailibilityInDay = Stream
//            .iterate(startOfCommandDay, dateTime-> dateTime.plusMinutes(store.getFrequenceSlotTime()))
//            .limit(ITERATION_PER_DAY)
//            // Retir les slots avant REF_FILTER_TIME
//            .filter(slot->slot.isAfter(refFilterTime.plusMinutes(commandPreparationTime)))
//
//            // Retire les slots déja pris par d'autres commandes
//            .filter(slot->!commands.contains(slot))
//
//            // Retire tous les slots qui ne sont pas dans les horaires du commerce
//            .filter(slot->storeSchedules
//                    .stream()
//                    .anyMatch(schedule-> CommonFunction.isSlotInStoreSchedule(slot,
//                            schedule,
//                            startOfCommandDay.toLocalDate(),
//                            endOfCommandDay.toLocalDate(),
//                            commandPreparationTime
//                    )))
//            .collect(Collectors.toList());
//
//    return slotAvailibilityInDay;
//  }
//
//  /**
//   * Calcul du nombre de créneaux pour commande disponable sur 24h pour 1 magasin
//   * Ne tient pas compte des commandes déja présentes
//   * @param store
//   * @return Integer
//   */
//  public Integer calculateNumberOfCommandSlotForOneDay(StoreEntity store) {
//    // Nombre de minute dans 1h
//    final int MINUTES_IN_HOUR = 60;
//
//    // Nombre d' heure dans 1 journée
//    final int HOUR_IN_DAY = 24;
//
//    // Nombre d'itération de commande par heure
//    final int ITERATION_PER_HOUR = MINUTES_IN_HOUR / store.getFrequenceSlotTime();
//
//    // Nombre d'iteration pour 24h
//    return ITERATION_PER_HOUR * HOUR_IN_DAY;
//  }
//}
