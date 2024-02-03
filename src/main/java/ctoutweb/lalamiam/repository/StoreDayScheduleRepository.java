package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface StoreDayScheduleRepository extends JpaRepository<StoreDayScheduleEntity, BigInteger> {
  //List<ScheduleEntity> findAllByStore(StoreEntity store);
  List<StoreDayScheduleEntity> findAllByStoreAndWeekDay(StoreEntity store, WeekDayEntity weekDay);
}
