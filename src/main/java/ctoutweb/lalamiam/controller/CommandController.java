package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.*;

import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/command")
public class CommandController {
  private final NewCommandService commandService;

  public CommandController(NewCommandService commandService) {
    this.commandService = commandService;
  }

  @GetMapping("/create/store/{storeId}")
  ResponseEntity<StoreProductsInformationDto> createCommand(@PathVariable Long storeId) {
    StoreProductsInformationDto addCommand = commandService.createCommand(storeId);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }
  @GetMapping("/update/store/{storeId}/command/{commandId}")
  ResponseEntity<StoreProductsInformationDto> updateCommand(@PathVariable Long storeId, @PathVariable Long commandId) {
    return new ResponseEntity<>(commandService.updateCommand(storeId, commandId), HttpStatus.OK);
  }
  @GetMapping("/store/{storeId}/command/{commandId}")
  ResponseEntity<RegisterCommandDto> getCommand(@PathVariable Long storeId, @PathVariable Long commandId) {
    return new ResponseEntity<>(commandService.getCommand(storeId, commandId), HttpStatus.OK);
  }

  @PostMapping("/persist-command")
  ResponseEntity<RegisterCommandDto> persistCommand(@RequestBody PersitCommandDto persitCommandInformation) {
    return new ResponseEntity<>(commandService.persistCommand(persitCommandInformation), HttpStatus.OK);
  }
}
