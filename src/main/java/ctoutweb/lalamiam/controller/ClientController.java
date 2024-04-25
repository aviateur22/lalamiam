package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.exception.ClientException;
import ctoutweb.lalamiam.model.dto.ClientPersitCommandDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.model.dto.StoreProductsInformationDto;

import ctoutweb.lalamiam.security.authentication.UserPrincipal;
import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

  @GetMapping("/client-id/{clientId}/store/{storeId}/command/{commandId}")
  ResponseEntity<RegisterCommandDto> getCommand(@PathVariable Long storeId, @PathVariable Long clientId, @PathVariable Long commandId) {

    validateClient(clientId);

    return new ResponseEntity<>(commandService.getCommand(storeId, commandId), HttpStatus.OK);
  }

  @PostMapping("/persist-command")
  ResponseEntity<RegisterCommandDto> persistCommand(@RequestBody ClientPersitCommandDto persitCommandInformation) {
    validateClient(persitCommandInformation.clientId());

    if(persitCommandInformation.commandId() != null) {
      validateCommandUpdate(persitCommandInformation);
    }
    return new ResponseEntity<>(commandService.clientPersistCommand(persitCommandInformation), HttpStatus.OK);
  }

  /**
   * Valide Le client entre Id du JWT et Id present dans le controller
   * @param clientId Long - identifiant client
   */
  private void validateClient(Long clientId) {
    // Todo faire test unitaire
    UserPrincipal userFromJwt = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if(!userFromJwt.getId().equals(clientId))
      throw new ClientException("Vous ne pouvez pas acceder à cette commande", HttpStatus.FORBIDDEN);
  }

  /**
   * Vérification si commande peut être mise à jour
   * @param clientPersitCommand ClientPersitCommandDto
   */
  private void validateCommandUpdate(ClientPersitCommandDto clientPersitCommand) {
    // Todo faire test

    // Récupération des anciennes données de la commande.
    RegisterCommandDto oldCommandInformation = commandService.getCommand(
            clientPersitCommand.storeId(),
            clientPersitCommand.commandId());

    int oldPreparationtime = oldCommandInformation.getCalculatedCommandInformation().getCommandPreparationTime();
    LocalDateTime oldSelectSlotTime = oldCommandInformation.getManualCommandInformation().getSlotTime();

    // Une commande ne peut plus être modifié par un client 5 min avant le debut de préparation
    LocalDateTime updateLimitTime = oldSelectSlotTime.minusMinutes(oldPreparationtime + 5);

    // Si heure de modification valide
    if(LocalDateTime.now().isAfter(updateLimitTime))
      throw new ClientException("Vous ne pouvez plus mettre à jour cette commande", HttpStatus.BAD_REQUEST);
  }
}
