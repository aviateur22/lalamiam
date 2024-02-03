package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.DailyStoreSchedule;
import ctoutweb.lalamiam.repository.StoreDayScheduleRepository;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreScheduleHelper {

  private final StoreDayScheduleRepository storeDayScheduleRepository;

  public StoreScheduleHelper(StoreDayScheduleRepository storeDayScheduleRepository) {
    this.storeDayScheduleRepository = storeDayScheduleRepository;
  }

  /**
   * Renvoie une liste de StoreSchedule sur une journée
   * @param store - StoreEntity
   * @param dayWeekNumber - Integer
   * @return List<DailyStoreSchedule>
   */
  public List<DailyStoreSchedule> getDayStoreSchedule(StoreEntity store, Integer dayWeekNumber) {
    // liste des horaires du commerce sur une semaine;
    return store.getStoreWeekDaySchedules()
            .stream()
            .filter(storeSchedule-> storeSchedule.getWeekDay().getId().equals(dayWeekNumber))
            .map(Factory::getDailyStoreSchedule
            )
            .toList();
  }
}
