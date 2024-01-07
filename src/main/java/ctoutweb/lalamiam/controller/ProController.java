package ctoutweb.lalamiam.controller;

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
  public ResponseEntity<String> createProfessional() {
    return new ResponseEntity<>("bonjour", HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getProfessionalInformation(@PathVariable("id")BigInteger id) {
    proService.getProfessionalInformation(id);
    return new ResponseEntity<>("bonjour", HttpStatus.OK);
  }

}
