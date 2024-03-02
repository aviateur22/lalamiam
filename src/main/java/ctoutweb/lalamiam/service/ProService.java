package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;

public interface ProService {
  public ProInformationDto addProfessional(AddProfessionalDto addProfessionalSchema);
  public ProInformationDto getProfessionalInformation(Long professionalId);

}
