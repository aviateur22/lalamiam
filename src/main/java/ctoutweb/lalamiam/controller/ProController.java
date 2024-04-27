package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.exception.ProException;
import ctoutweb.lalamiam.helper.ProHelper;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.service.NewCommandService;
import ctoutweb.lalamiam.service.ProService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pro")
public class ProController {
  private final ProService proService;
  private final NewCommandService commandService;
  private final ProHelper proHelper;

  public ProController(
          ProService proService,
          NewCommandService commandService,
          ProHelper proHelper) {
    this.proService = proService;
    this.commandService = commandService;
    this.proHelper = proHelper;
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

  @GetMapping("/command/pro-id/{proId}/store-id/{storeId}/get-store-product-to-create-command")
  ResponseEntity<StoreProductsInformationDto> getStoreProductToCreateCommand(@PathVariable Long storeId, @PathVariable Long proId) {

    // Validation professionel
    if(!proHelper.isProfessionalValid(proId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    if(!proHelper.isProWorkingInStore(proId, storeId))
      throw new ProException("Vous n'êtes pas rattaché au commerce", HttpStatus.FORBIDDEN);

    StoreProductsInformationDto addCommand = commandService.getStoreProductToCreateCommand(storeId);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }

  @GetMapping("/command/pro-id/{proId}/store-id/{storeId}/command-id/{commandId}/get-store-product-to-update-command")
  ResponseEntity<StoreProductsInformationDto> getStoreProductToUpdateCommand(@PathVariable Long storeId, @PathVariable Long commandId,  @PathVariable Long proId) {

    // Validation professionel
    if(!proHelper.isProfessionalValid(proId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    if(!proHelper.isProWorkingInStore(proId, storeId))
      throw new ProException("Vous n'êtes pas rattaché au commerce", HttpStatus.FORBIDDEN);

    if(!proHelper.isCommandVisibleByPro(proId, commandId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    return new ResponseEntity<>(commandService.getStoreProductToUpdateCommand(storeId, commandId), HttpStatus.OK);
  }

  @GetMapping("/command/pro-id/{proId}/store-id/{storeId}/command-id/{commandId}")
  ResponseEntity<RegisterCommandDto> getCommand(@PathVariable Long storeId, @PathVariable Long commandId, @PathVariable Long proId) {

    // Validation professionel
    if(!proHelper.isProfessionalValid(proId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    if(!proHelper.isProWorkingInStore(proId, storeId))
      throw new ProException("Vous n'êtes pas rattaché au commerce", HttpStatus.FORBIDDEN);

    if(!proHelper.isCommandVisibleByPro(proId, commandId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);


    return new ResponseEntity<>(commandService.getCommand(storeId, commandId), HttpStatus.OK);
  }

  @PostMapping("/command/persist-command")
  ResponseEntity<RegisterCommandDto> persistCommand(@RequestBody ProPersitCommandDto persitCommandInformation) {

    Long proId = persitCommandInformation.proId();
    Long commandId = persitCommandInformation.commandId();
    Long storeId = persitCommandInformation.storeId();

    // Validation professionel
    if(!proHelper.isProfessionalValid(proId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    if(commandId != null) {
      if(!proHelper.isProWorkingInStore(proId, storeId))
        throw new ProException("Vous n'êtes pas rattaché au commerce", HttpStatus.FORBIDDEN);

      if(!proHelper.isCommandVisibleByPro(proId, commandId))
        throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(commandService.proPersistCommand(persitCommandInformation), HttpStatus.CREATED);
  }

  @PutMapping("/command/update-status")
  ResponseEntity<RegisterCommandDto> updateCommandStatus(@RequestBody ProUpdateCommandStatusDto proUpdateCommandStatus) {
    Long proId = proUpdateCommandStatus.proId();
    Long commandId = proUpdateCommandStatus.commandId();
    Long storeId = proUpdateCommandStatus.storeId();

    // Validation professionel
    if(!proHelper.isProfessionalValid(proId))
      throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    if(commandId != null) {
      if(!proHelper.isProWorkingInStore(proId, storeId))
        throw new ProException("Vous n'êtes pas rattaché au commerce", HttpStatus.FORBIDDEN);

      if(!proHelper.isCommandVisibleByPro(proId, commandId))
        throw new ProException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(commandService.updateCommandStatus(proUpdateCommandStatus), HttpStatus.OK);

  }

}
