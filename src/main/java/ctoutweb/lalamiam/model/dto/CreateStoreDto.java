package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.WeeklyStoreSchedule;

import java.util.List;

/**
 * Réponse à la création d'un Store
 */
public record CreateStoreDto(
  Long proId,
  Long StoreId,
  String name,
  String adress,
  String city,
  String cp,
  List<StoreScheduleDto> storeSchedules,
  Integer frequenceSlotTime) {}
