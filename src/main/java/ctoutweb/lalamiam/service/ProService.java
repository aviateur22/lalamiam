package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;

import java.math.BigInteger;

public interface ProService {
  public ProInformationDto addProfessional(AddProfessionalDto addProfessionalSchema);
  public ProInformationDto getProfessionalInformation(BigInteger professionalId);

}
