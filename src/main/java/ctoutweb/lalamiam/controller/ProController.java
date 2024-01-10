package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.service.ProService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("api/lalamiam/v1/pro")
public class ProController {

  private final ProService proService;

  public ProController(ProService proService) {
    this.proService = proService;
  }

  @PostMapping("/")
  public ResponseEntity<ProInformationDto> createProfessional(@RequestBody AddProfessionalSchema addProfessionalSchema) {
    ProInformationDto addProfessional = proService.addProfessional(addProfessionalSchema);
    return new ResponseEntity<>(addProfessional, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProInformationDto> getProfessionalInformation(@PathVariable("id")BigInteger id) {
    ProInformationDto proInformationDto = proService.getProfessionalInformation(id);
    return new ResponseEntity<>(proInformationDto, HttpStatus.OK);
  }

}
