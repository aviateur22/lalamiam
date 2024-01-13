package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.StoreService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class StoreServiceImp implements StoreService {

  private final StoreRepository storeRepository;
  private final ProRepository proRepository;
  public StoreServiceImp(
          StoreRepository storeRepository,
          ProRepository proRepository) {
    this.storeRepository = storeRepository;
    this.proRepository = proRepository;
  }

  @Override
  public StoreEntity createStore(AddStoreSchema addStoreSchema) {
    String name = addStoreSchema.name();
    String adress = addStoreSchema.Adress();
    String city = addStoreSchema.city();
    String cp = addStoreSchema.cp();
    BigInteger proId = addStoreSchema.proId();

    if(CommonFunction.isNullOrEmpty(name)||
            CommonFunction.isNullOrEmpty(adress) ||
            CommonFunction.isNullOrEmpty(city) ||
            CommonFunction.isNullOrEmpty(cp) ||
            proId == null) throw new RuntimeException("données manquante");

    // Vérification existence pro
    proRepository.findById(addStoreSchema.proId()).orElseThrow(()->new RuntimeException("Le professionel n'existe pas"));

    StoreEntity createdStore = storeRepository.save(new StoreEntity(addStoreSchema));

    return createdStore;
  }
}
