package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.dto.ProInformationDto;

import java.math.BigInteger;

public interface ProService {
  public ProInformationDto addProfessional(AddProfessionalSchema addProfessionalSchema);
  public ProInformationDto getProfessionalInformation(BigInteger professionalId);
}
