package helper;

import ctoutweb.lalamiam.model.DailyStoreSchedule;
import ctoutweb.lalamiam.model.WeeklyStoreSchedule;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.model.dto.CreateStoreDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.StoreService;

import java.time.LocalTime;
import java.util.List;

public class StoreHelper {

  private final StoreService storeService;

  public StoreHelper(
          StoreService storeService

  ) {
    this.storeService = storeService;
  }

  /**
   * Création d'un Store
   * @param createdPro - ProInformationDto
   * @return
   */
  public CreateStoreDto createStore(ProInformationDto createdPro) {

    List<WeeklyStoreSchedule> storeWeeklySchedules = createStoreWeeklySchedule();

    AddStoreDto addStoreSchema = new AddStoreDto(
            createdPro.id(),
            "magasin",
            "rue des carriere",
            "auterive",
            "31190",
            storeWeeklySchedules,
            10);
    CreateStoreDto createdStore = storeService.createStore(addStoreSchema);
    return createdStore;
  }

  /**
   * Génération d'horaires pour le commerce
   * @return
   */
  private List<WeeklyStoreSchedule> createStoreWeeklySchedule() {
    // Jours
    List<Integer> days = List.of(2, 3, 4, 5, 6);
    List<Integer> dayMonday = List.of(1);
    List<Integer> daySunday = List.of(7);

    // Horaires
    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));

    // Horaires du commerce
    return List.of(
            new WeeklyStoreSchedule(days, storeScheduleMorning),
            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
    );
  }

}
