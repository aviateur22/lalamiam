package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.transaction.StoreTransaction;
import ctoutweb.lalamiam.service.StoreService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
@Service
public class StoreServiceImp implements StoreService {
 private final StoreTransaction storeTransaction;
  public StoreServiceImp(StoreTransaction storeTransaction) {
    this.storeTransaction = storeTransaction;
  }

  @Override
  public StoreEntity createStore(AddStoreDto addStore) {
    String name = addStore.name();
    String adress = addStore.adress();
    String city = addStore.city();
    String cp = addStore.cp();
    BigInteger proId = addStore.proId();

    if(CommonFunction.isNullOrEmpty(name)||
            CommonFunction.isNullOrEmpty(adress) ||
            CommonFunction.isNullOrEmpty(city) ||
            CommonFunction.isNullOrEmpty(cp) ||
            proId == null) throw new RuntimeException("données manquantes");

    BigInteger storeId = storeTransaction.SaveStore(addStore);
    StoreEntity store = storeTransaction.getCompleteStoreInformation(storeId);
    return store;
  }
}
