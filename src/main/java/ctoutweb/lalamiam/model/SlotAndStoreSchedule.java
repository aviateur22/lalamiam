//package ctoutweb.lalamiam.model;
//
//import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * Données commerce sur les créneaux et horaires
// * @param storeSchedules List<StoreDayScheduleEntity> - Liste des horaires du commerce sur une journée
// * @param slotTimeAvailibilityInOneDay List<LocalDateTime>  - Liste des créneaux dispo sur 24h de 00h00 à 24h00
// *                                 (sans tenir compte des contraintes horaires et slotBusyDayList)
// * @param slotBusyDayList - Liste des créneaux déja utilisés
// */
//public record SlotAndStoreSchedule(
//        List<StoreDayScheduleEntity> storeSchedules,
//        List<LocalDateTime> slotTimeAvailibilityInOneDay,
//        List<LocalDateTime> slotBusyDayList
//) {
//}
