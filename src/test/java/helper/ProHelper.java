package helper;

import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.service.ProService;

public class ProHelper {

  private final ProService proService;

  public ProHelper(ProService proService) {
    this.proService = proService;
  }

}
