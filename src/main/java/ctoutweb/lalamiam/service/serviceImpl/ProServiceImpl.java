package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.mapper.ProInformationMapper;
import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.math.BigInteger;

@Service
public class ProServiceImpl implements ProService {

  private final ProRepository proRepository;

  private final StoreRepository storeRepository;
  private final ProInformationMapper proInformationMapper;

  public ProServiceImpl(ProRepository proRepository, StoreRepository storeRepository, ProInformationMapper proInformationMapper) {
    this.proRepository = proRepository;
    this.storeRepository = storeRepository;
    this.proInformationMapper = proInformationMapper;
  }

  @Override
  public ProInformationDto addProfessional(AddProfessionalSchema addProfessionalInfo) {

    String password = addProfessionalInfo.password();
    String email = addProfessionalInfo.email();

    if(password == null || password.isEmpty() || email == null || email.isEmpty())
      throw new RuntimeException("Données Professionnelles incomplete");

    ProEntity addPro = proRepository.save(new ProEntity(addProfessionalInfo));
    return proInformationMapper.apply(addPro);
  }

  @Override
  public ProInformationDto getProfessionalInformation(BigInteger professionalId) {
    ProEntity proEntity = proRepository.findById(professionalId).orElseThrow(()-> new RuntimeException("pas connu"));
    return proInformationMapper.apply(proEntity);
  }

  @Override
  public StoreEntity createStore(AddStoreSchema addStoreSchema) {

    String name = addStoreSchema.name();
    String adress = addStoreSchema.Adress();
    String city = addStoreSchema.city();
    String cp = addStoreSchema.cp();
    ProEntity pro = addStoreSchema.pro();

    if(CommonFunction.isNullOrEmpty(name)||
            CommonFunction.isNullOrEmpty(adress) ||
            CommonFunction.isNullOrEmpty(city) ||
            CommonFunction.isNullOrEmpty(cp) ||
            pro == null) throw new RuntimeException("données manquante");

    StoreEntity createdStore = storeRepository.save(new StoreEntity(addStoreSchema));

    return createdStore;
  }
}
