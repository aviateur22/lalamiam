package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.service.ProService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lalamiam/v1/pro")
public class ProController {

  private final ProService proService;

  public ProController(ProService proService) {
    this.proService = proService;
  }

  @PostMapping("")
  public ResponseEntity<ProInformationDto> createProfessional(@RequestBody AddProfessionalDto addProfessionalSchema) {
    ProInformationDto addProfessional = proService.addProfessional(addProfessionalSchema);
    return new ResponseEntity<>(addProfessional, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProInformationDto> getProfessionalInformation(@PathVariable("id")Long id) {
    ProInformationDto proInformationDto = proService.getProfessionalInformation(id);
    return new ResponseEntity<>(proInformationDto, HttpStatus.OK);
  }

}
