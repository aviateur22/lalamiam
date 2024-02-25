package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.*;

import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("api/lalamiam/v1/command")
public class CommandController {
  private final NewCommandService commandService;

  public CommandController(NewCommandService commandService) {
    this.commandService = commandService;
  }

  @GetMapping("/create/{storeId}")
  ResponseEntity<StoreProductsInformationDto> createCommand(@RequestParam BigInteger storeId) {
    StoreProductsInformationDto addCommand = commandService.createCommand(storeId);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }
  @GetMapping("/update/{storeId}/command/{commandId}")
  ResponseEntity<StoreProductsInformationDto> updateCommand(@RequestParam BigInteger storeId, BigInteger commandId) {
    return new ResponseEntity<>(commandService.updateCommand(storeId, commandId), HttpStatus.OK);
  }
  @GetMapping("/get/{storeId}/command/{commandId}")
  ResponseEntity<RegisterCommandDto> getCommand(@RequestParam BigInteger storeId, BigInteger commandId) {
    return new ResponseEntity<>(commandService.getCommand(storeId, commandId), HttpStatus.OK);
  }

}
