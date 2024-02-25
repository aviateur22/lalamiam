package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
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
}
