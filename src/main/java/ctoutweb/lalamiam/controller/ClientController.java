package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.exception.ClientException;
import ctoutweb.lalamiam.helper.ClientHelper;
import ctoutweb.lalamiam.model.dto.ClientPersitCommandDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.model.dto.StoreProductsInformationDto;

import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {
  private final NewCommandService commandService;
  private final ClientHelper clientHelper;

  public ClientController(NewCommandService commandService, ClientHelper clientHelper) {
    this.commandService = commandService;
    this.clientHelper = clientHelper;
  }

  @GetMapping("/command/client-id/{clientId}/store-id/{storeId}/get-store-product-to-create-command")
  ResponseEntity<StoreProductsInformationDto> getStoreProductToCreateCommand(
          @PathVariable Long storeId,
          @PathVariable Long clientId
  ) {
    // Validation clientId Versus JWT
    if(!clientHelper.isUserIdEqualToJwtUserId(clientId))
      throw new ClientException("Vous ne pouvez pas créer cette commande", HttpStatus.FORBIDDEN);

    StoreProductsInformationDto addCommand = commandService.getStoreProductToCreateCommand(storeId);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }

  @GetMapping("/command/client-id/{clientId}/store-id/{storeId}/command-id/{commandId}/get-store-product-to-update-command")
  ResponseEntity<StoreProductsInformationDto> getStoreProductToUpdateCommand(
          @PathVariable Long clientId,
          @PathVariable Long storeId,
          @PathVariable Long commandId
  ) {
    // Validation clientId Versus JWT
    if(!clientHelper.isUserIdEqualToJwtUserId(clientId))
      throw new ClientException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    if(!clientHelper.isCommandBelondToUser(clientId, commandId))
      throw new ClientException("Vous n'êtes pas rattaché à cette commande", HttpStatus.FORBIDDEN);

    return new ResponseEntity<>(commandService.getStoreProductToUpdateCommand(storeId, commandId), HttpStatus.OK);
  }

  @GetMapping("/command/client-id/{clientId}/store-id/{storeId}/command-id/{commandId}")
  ResponseEntity<RegisterCommandDto> getCommand(
          @PathVariable Long storeId,
          @PathVariable Long clientId,
          @PathVariable Long commandId
  ) {

    // Validation clientId Versus JWT
    if(!clientHelper.isUserIdEqualToJwtUserId(clientId))
      throw new ClientException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    // Validation rattachement commande - client
    if(!clientHelper.isCommandBelondToUser(clientId, commandId))
      throw new ClientException("Vous n'êtes pas rattaché à cette commande", HttpStatus.FORBIDDEN);

    return new ResponseEntity<>(commandService.getCommand(storeId, commandId), HttpStatus.OK);
  }

  @PostMapping("/command/persist-command")
  ResponseEntity<RegisterCommandDto> persistCommand(@RequestBody ClientPersitCommandDto persitCommandInformation) {

    Long clientId = persitCommandInformation.clientId();
    Long commandId = persitCommandInformation.commandId();

    // Validation clientId Versus JWT
    if(!clientHelper.isUserIdEqualToJwtUserId(clientId))
      throw new ClientException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);

    // Verification si heure de mofication valide
    if(commandId != null) {
      // Validation rattachement commande - client
      if(!clientHelper.isCommandBelondToUser(clientId, commandId))
        throw new ClientException("Vous n'êtes pas rattaché à cette commande", HttpStatus.FORBIDDEN);

      // Validation heure de modification de la commande
      if(!clientHelper.isTimeValidadBeforeUpdateCommand(persitCommandInformation))
        throw new ClientException("Vous ne pouvez plus mettre à jour cette commande", HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(commandService.clientPersistCommand(persitCommandInformation), HttpStatus.OK);
  }

}
