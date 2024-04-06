package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.ClientPersitCommandDto;
import ctoutweb.lalamiam.model.dto.ProPersitCommandDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.model.dto.StoreProductsInformationDto;
import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/client/command")
public class ClientController {

  private final NewCommandService commandService;

  public ClientController(NewCommandService commandService) {
    this.commandService = commandService;
  }

  @GetMapping("/create/store/{storeId}")
  ResponseEntity<StoreProductsInformationDto> createCommand(@PathVariable Long storeId) {
    StoreProductsInformationDto addCommand = commandService.createCommand(storeId);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }

  @GetMapping("/store/{storeId}/command/{commandId}")
  ResponseEntity<RegisterCommandDto> getCommand(@PathVariable Long storeId, @PathVariable Long commandId) {
    return new ResponseEntity<>(commandService.getCommand(storeId, commandId), HttpStatus.OK);
  }

  @PostMapping("/client-id/{clienId}/persist-command")
  ResponseEntity<RegisterCommandDto> persistCommand(@RequestBody ClientPersitCommandDto persitCommandInformation) {
    return new ResponseEntity<>(commandService.clientPersistCommand(persitCommandInformation), HttpStatus.OK);
  }
}
