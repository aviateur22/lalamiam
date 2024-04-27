package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.dto.ClientPersitCommandDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.ClientCommandRepository;
import ctoutweb.lalamiam.repository.entity.ClientCommandEntity;
import ctoutweb.lalamiam.security.authentication.UserPrincipal;
import ctoutweb.lalamiam.service.ClientService;
import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ClientHelper {

  private final ClientService clientService;
  private final ClientCommandRepository clientCommandRepository;
  private final NewCommandService commandService;

  public ClientHelper(
          ClientService clientService,
          ClientCommandRepository clientCommandRepository,
          NewCommandService commandService
  ) {
    this.clientService = clientService;
    this.clientCommandRepository = clientCommandRepository;
    this.commandService = commandService;
  }

  /**
   * Vérifcation de l'identification client
   * @param clientId Long - Identifiant client
   * @return Boolean
   */
  public Boolean isUserIdEqualToJwtUserId(Long clientId) {
    // Recherhe client
    if(!clientService.findClient(clientId).isPresent())
      return  false;

    // Todo faire test unitaire
    UserPrincipal userFromJwt = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return userFromJwt.getId().equals(clientId);
  }

  /**
   * Vérification si commande appartient au client
   * @param clientId Long - Identifiant client
   * @param commandId Long - Identifiant commande
   * @return Boolean
   */
  public Boolean isCommandBelondToUser(Long clientId, Long commandId) {
    Optional<ClientCommandEntity> findClientCommand = clientCommandRepository.findOneByUserAndCommand(
            Factory.getUSer(clientId),
            Factory.getCommand(commandId));

    return findClientCommand.isPresent();
  }

  /**
   * Vérification si heure de modification commande valide
   * @param clientPersitCommand ClientPersitCommandDto
   * @return Boolean
   */
  public Boolean isTimeValidadBeforeUpdateCommand(ClientPersitCommandDto clientPersitCommand) {
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
    return LocalDateTime.now().isBefore(updateLimitTime);
  }
}
