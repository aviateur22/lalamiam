package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.repository.entity.StoreEntity;

public interface StoreService {
  public StoreEntity createStore(AddStoreDto addStoreSchema);

}
