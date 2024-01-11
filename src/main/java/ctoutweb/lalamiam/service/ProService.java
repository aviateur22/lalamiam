package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.repository.entity.StoreEntity;

import java.beans.IntrospectionException;
import java.math.BigInteger;

public interface ProService {
  public ProInformationDto addProfessional(AddProfessionalSchema addProfessionalSchema);
  public ProInformationDto getProfessionalInformation(BigInteger professionalId);
  public StoreEntity createStore(AddStoreSchema addStoreSchema);

  //public StoreInformationDto getStoreInformation(AddStoreSchema addStoreSchema);
}
