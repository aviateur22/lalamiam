package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.AddProfessionalSchema;
import ctoutweb.lalamiam.model.dto.ProInformation;

import java.math.BigInteger;

public interface ProService {
  public void addProfessional(AddProfessionalSchema addProfessionalSchema);
  public ProInformation getProfessionalInformation(BigInteger professionalId);
}
