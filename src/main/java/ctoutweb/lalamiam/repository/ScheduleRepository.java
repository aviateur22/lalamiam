package ctoutweb.lalamiam.repository;

import ctoutweb.lalamiam.repository.entity.ScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, BigInteger> {
  List<ScheduleEntity> findAllByStore(StoreEntity store);
}
