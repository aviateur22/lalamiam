package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductSchema;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;

import java.math.BigInteger;
import java.util.List;

public interface StoreService {
  public StoreEntity createStore(AddStoreSchema addStoreSchema);

}
