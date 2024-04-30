package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.exception.CommandException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.helper.NewSlotHelper;
import ctoutweb.lalamiam.model.CommandStatus;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.NewCommandService;
import ctoutweb.lalamiam.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewCommandServiceImp implements NewCommandService {
  private static final Logger LOGGER = LogManager.getLogger();
  private final NewCommandServiceHelper commandServiceHelper;
  private final CommandTransactionSession commandTransactionSession;
  private final CommandRepository commandRepository;
  private final ProductService productService;
  private final NewSlotHelper slotHelper;

  public NewCommandServiceImp(
          NewCommandServiceHelper newCommandServiceHelper,
          CommandTransactionSession commandTransactionSession,
          CommandRepository commandRepository,
          ProductService productService,
          NewSlotHelper slotHelper
  ) {
    this.commandServiceHelper = newCommandServiceHelper;
    this.commandTransactionSession = commandTransactionSession;
    this.commandRepository = commandRepository;
    this.productService = productService;
    this.slotHelper = slotHelper;
  }

  @Override
  public StoreProductsInformationDto getStoreProductToCreateCommand(Long storeId) {
    return getStoreProductsForCommand(storeId, null);
  }

  @Override
  public StoreProductsInformationDto getStoreProductToUpdateCommand(Long storeId, Long commandId) {
    // Récupération de la commande
    CommandEntity command = commandTransactionSession.getCommand(commandId);

    if(command == null) throw new CommandException("La commande à mettre à jour n'existe pas", HttpStatus.NOT_FOUND);

    // Si commande existante alors vérification du storeId
    if(!command.getStore().getId().equals(storeId))
      throw new CommandException("Cette commande n'est pas rattaché au commerce", HttpStatus.BAD_REQUEST);

    return getStoreProductsForCommand(storeId, command);
  }

  @Override
  public RegisterCommandDto getCommand(Long storeId, Long commandId) {
    // Récupération de la commande
    CommandEntity command = commandTransactionSession.getCommand(commandId);

    // Si commande existante alors vérification du storeIs
    if(command == null)
      throw new CommandException("Cette commande n'existe pas", HttpStatus.NOT_FOUND);

    // Si commande existante alors vérification du storeIs
    if(command != null && !command.getStore().getId().equals(storeId))
      throw new CommandException("Cette commande n'est pas rattaché au commerce", HttpStatus.NOT_FOUND);

    // Recherche d'une commande
    return commandServiceHelper.findRegisterCommandInformation(command);
  }

  @Override
  public RegisterCommandDto updateCommandStatus(ProUpdateCommandStatusDto proUpdateCommandStatus) {
    CommandEntity findCommand = commandRepository.findById(proUpdateCommandStatus.commandId()).orElseThrow(()->
            new CommandException("La commande à mettre à jour n'existe pas", HttpStatus.NOT_FOUND));

    // Si commande existante alors vérification du storeId
    if(!findCommand.getStore().getId().equals(proUpdateCommandStatus.storeId()))
      throw new CommandException("Cette commande n'est pas rattaché au commerce", HttpStatus.BAD_REQUEST);

    // Vérification validité du statut
    if(!CommandStatus.isStatusValid(proUpdateCommandStatus.commandStatus()))
      throw new CommandException("Le statut de la commande n'existe pas", HttpStatus.BAD_REQUEST);

    // Action faite par un pro
    final boolean IS_PRO_UPDATE_STATUS = true;

    // Mise a jour du statut et mise a jour du suivi de l'évolution du statut
    commandTransactionSession.updateCommandStatus(findCommand, proUpdateCommandStatus, IS_PRO_UPDATE_STATUS);

    // Récuperation de la commande
    return getCommand(proUpdateCommandStatus.storeId(), proUpdateCommandStatus.commandId());
  }

  @Override
  public StoreProductsInformationDto getStoreProductsForCommand(Long storeId, CommandEntity command) {
    // Recherche d'une commande
    RegisterCommandDto registerCommand = commandServiceHelper.findRegisterCommandInformation(command);

    // Recherche des produits du commerce avec ajout des quantité deja présente dans la commande
    List<ProductWithQuantityDto> findStoreProductsWithQuantity = commandServiceHelper.getStoreProductsWithCommandQuantity(
            storeId,
            registerCommand);

    return Factory.getCommandInformationDto(registerCommand, findStoreProductsWithQuantity);
  }

  @Override
  public List<LocalDateTime> getStoreSlotAvailibility (CommandInformationDto commandInformation) {
    // Todo mettre a jour test unitaire
    // Date de la commande
    LocalDate refCommandDate = commandInformation.commandId() == null ? LocalDate.now() : commandInformation.commandDate();
    LOGGER.info(()->String.format("Date de reference de la commande: %s", refCommandDate));

    if(refCommandDate == null)
      throw new CommandException("Date de référence manquante dans la commande", HttpStatus.INTERNAL_SERVER_ERROR);

    // Début de journée jour de commande (00h01)
    LocalDateTime startOfCommandDay = refCommandDate.atStartOfDay();
    LOGGER.info(()->String.format("startOfCommandDay: %s", startOfCommandDay));

    // Fin de journnée jour de commande
    LocalDateTime endOfCommandDay = LocalDateTime.from(startOfCommandDay).with(LocalTime.MAX);
    LOGGER.info(()->String.format("endOfCommandDay: %s", endOfCommandDay));

    // Recherche de l'horaire de reference pour commencer à filtrer les créneaux
    LocalDateTime refFilterTime = commandInformation.consultationDate().toLocalDate().equals(refCommandDate) ?
            commandInformation.consultationDate(): startOfCommandDay;
    LOGGER.info(()->String.format("refFilterTime: %s", refFilterTime));


    // Recherche des commandes du jours
    List<CommandEntity> commandsOfTheDay  = commandRepository.findCommandsByStoreIdDate(
            startOfCommandDay,
            endOfCommandDay,
            commandInformation.storeId()
    );
    LOGGER.info(()->String.format("commandsOfTheDay: %s", commandsOfTheDay));

    // Calcul du temps de préparation
    Integer commandPreparationTime = commandServiceHelper.calculateCommandPreparationTime(commandInformation.productsSelected());
    LOGGER.info(()->String.format("commandPreparationTime: %s", commandPreparationTime));

    // Informations nécessaires pour calculer les slots disponibles
    slotHelper.setCommandPreparationTime(commandPreparationTime);
    slotHelper.setEndOfCommandDay(endOfCommandDay);
    slotHelper.setStartOfCommandDay(startOfCommandDay);
    slotHelper.setRefFilterTime(refFilterTime);

    // Renvoi une liste de slot disponible filtré par rapport aux horaires d'ouverture et les commandes déja existantes
    List<LocalDateTime> storeSlotAvailibility = slotHelper.getStoreSlotAvailibility(commandInformation.storeId(), commandsOfTheDay);

    // Si modification d'une commande alors rajout du slot de la commande initiale
    if(commandInformation.commandId() != null) {
      RegisterCommandDto initialCommand = getCommand(commandInformation.storeId(), commandInformation.commandId());
      storeSlotAvailibility.add(initialCommand.getManualCommandInformation().getSlotTime());
    }

    // Tri de la liste des creneaux
    storeSlotAvailibility.sort(LocalDateTime::compareTo);

    return storeSlotAvailibility;
  }

  @Override
  public RegisterCommandDto proPersistCommand(ProPersitCommandDto persitCommand) {
    // Todo Implementer les tests unitaires

    final int COMMAND_CODE_LENGTH = 5;

    ProductSelectInformationDto productSelectInformation = Factory.getProductSelectInformationDto(
            persitCommand.selectProducts(),
            persitCommand.clientPhone()
    );


    CommandInformationDto commandInformation = Factory.getCommandInformationDto(
      persitCommand.storeId(),
      persitCommand.commandId(),
      persitCommand.commandDate(),
      persitCommand.consultationDate(),
      persitCommand.selectProducts(),
      persitCommand.selectSlotTime()
    );

    // Verification validité de la commande
    validateProductsSelection(commandInformation.storeId(), commandInformation.commandId(), productSelectInformation);

    // Validation du creneau de la commande
    validateSlot(commandInformation);

    // Code de la commande
    String commandCode = commandServiceHelper.generateCode(COMMAND_CODE_LENGTH);

    // Calcul du temps de preparation
    Integer preparationTime = commandServiceHelper.calculateCommandPreparationTime(persitCommand.selectProducts());

    // Calcul du prix de la commande
    Double commandPrice = commandServiceHelper.calculateCommandPrice(persitCommand.selectProducts());

    // Calcul du nombre de produit
    Integer numberOfProductInCommand = commandServiceHelper.calculateNumberOfProductInCommand(persitCommand.selectProducts());

    CommandEntity command = persitCommand.commandId() == null ?

      commandTransactionSession.proSaveCommand(
        Factory.getCommandInformationToSave(
          persitCommand,
          commandCode,
          preparationTime,
          numberOfProductInCommand,
          commandPrice,
          persitCommand.proId()

        ))
            :
      commandTransactionSession.proUpdateCommand(
        Factory.getCommandInformationToUpdate(
          persitCommand,
          preparationTime,
          numberOfProductInCommand,
          commandPrice
        ));

    return commandServiceHelper.findRegisterCommandInformation(command);
  }

  @Override
  public RegisterCommandDto clientPersistCommand(ClientPersitCommandDto persitCommand) {
    // Todo Implementer les tests unitaires
    // Todo verifier clientId
    // Todo verifier existance client

    final int COMMAND_CODE_LENGTH = 5;

    ProductSelectInformationDto productSelectInformation = Factory.getProductSelectInformationDto(
            persitCommand.selectProducts(),
            persitCommand.clientPhone()
    );


    CommandInformationDto commandInformation = Factory.getCommandInformationDto(
            persitCommand.storeId(),
            persitCommand.commandId(),
            persitCommand.commandDate(),
            persitCommand.consultationDate(),
            persitCommand.selectProducts(),
            persitCommand.selectSlotTime()
    );

    // Verification client
    if(persitCommand.clientId() == null)
      throw new CommandException("Identifiant client inconnu", HttpStatus.BAD_REQUEST);

    // Verification validité de la commande
    validateProductsSelection(commandInformation.storeId(), commandInformation.commandId(), productSelectInformation);

    // Validation du creneau de la commande
    validateSlot(commandInformation);

    // Code de la commande
    String commandCode = commandServiceHelper.generateCode(COMMAND_CODE_LENGTH);

    // Calcul du temps de preparation
    Integer preparationTime = commandServiceHelper.calculateCommandPreparationTime(persitCommand.selectProducts());

    // Calcul du prix de la commande
    Double commandPrice = commandServiceHelper.calculateCommandPrice(persitCommand.selectProducts());

    // Calcul du nombre de produit
    Integer numberOfProductInCommand = commandServiceHelper.calculateNumberOfProductInCommand(persitCommand.selectProducts());

    CommandEntity command = persitCommand.commandId() == null ?

            commandTransactionSession.clientSaveCommand(
                    Factory.getCommandInformationToSave(
                            persitCommand,
                            commandCode,
                            preparationTime,
                            numberOfProductInCommand,
                            commandPrice
                    ))
            :
            commandTransactionSession.clientUpdateCommand(
                    Factory.getCommandInformationToUpdate(
                            persitCommand,
                            preparationTime,
                            numberOfProductInCommand,
                            commandPrice
                    ), persitCommand.clientId());

    return commandServiceHelper.findRegisterCommandInformation(command);
  }
  @Override
  public void validateProductsSelection(Long storeId, Long commandId, ProductSelectInformationDto productSelectInformation) {
    if(storeId == null)  throw new RuntimeException("Identifiant commerce obligatoire");

    // N°Tel
    if(productSelectInformation.clientPhone() == null || productSelectInformation.clientPhone().isEmpty())
      throw new RuntimeException("Le numéro de téléphone est obligatoire");

    if(commandId != null) {
      CommandEntity command = commandTransactionSession.getCommand(commandId);

      if (command == null) throw new CommandException("La commande n'existe pas", HttpStatus.BAD_REQUEST);

      // Si commande existante alors vérification du storeId
      if(!command.getStore().getId().equals(storeId))
        throw new CommandException("Cette commande n'est pas rattaché au commerce", HttpStatus.BAD_REQUEST);
    }

    // Verifier liste de produits > 0 et produit appartient au commerce
    if(productSelectInformation.productSelectList() == null || productSelectInformation.productSelectList().size() == 0)
      throw new RuntimeException("La commande ne peut pas être vide");

    productSelectInformation.productSelectList().forEach(productWithQuantity -> {
      ProductEntity product = productService.findProduct(productWithQuantity.getProductId());
      // Vérification que le produit appartienne au commerce
      if(!product.getStore().getId().equals(storeId))
        throw new CommandException(String.format("Le produit %s n'est pas référencé dans ce commerce", product.getName()), HttpStatus.BAD_REQUEST);

      // Vérification que le produit est disponible
      if(!product.getIsAvail())
        throw new CommandException(String.format("Le produit %s n'est plus disponible", product.getName()), HttpStatus.BAD_REQUEST);

      // Vérification de la quantité de chaque produit
      if(productWithQuantity.getProductQuantity() < 1)
        throw new CommandException(String.format("Merci d'indiquer la quantité pour le produit %s", product.getName()), HttpStatus.BAD_REQUEST);
    });
  }

  @Override
  public void validateSlot(CommandInformationDto commandInformation) {

    // Récuperation des créneaux disponible
    List<LocalDateTime> storeAvailibiltySlots = getStoreSlotAvailibility(commandInformation);

    LOGGER.info(()->String.format("Liste des créneaux dispo pour la commande %s", storeAvailibiltySlots));
    LOGGER.info(()->String.format("Heure de la commande %s", commandInformation.selectSlotTime()));



    if(storeAvailibiltySlots
            .stream()
            .filter(storeSlot->commandInformation.selectSlotTime().equals(storeSlot))
            .collect(Collectors.toList())
            .size() == 0) throw new CommandException("Le créneau demandé n'est pas disponible", HttpStatus.BAD_REQUEST);
  }

  @Override
  public List<DashboardCommandDto> getDashboardCommands(GetDashboardCommandDto getDashboard) {
    return null;
  }
}
