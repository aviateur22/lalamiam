package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.exception.CommandException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.helper.NewSlotHelper;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.NewCommandService;
import ctoutweb.lalamiam.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewCommandServiceImp implements NewCommandService {
  private final NewCommandServiceHelper commandServiceHelper;
  private final CommandTransactionSession commandTransactionSession;
  private final CommandRepository commandRepository;
  private final ProductService productService;
  private final NewSlotHelper slotHelper;

  public NewCommandServiceImp(
          NewCommandServiceHelper newCommandServiceHelper,
          CommandTransactionSession commandTransactionSession,
          CommandRepository commandRepository, ProductService productService,
          NewSlotHelper slotHelper
  ) {
    this.commandServiceHelper = newCommandServiceHelper;
    this.commandTransactionSession = commandTransactionSession;
    this.commandRepository = commandRepository;
    this.productService = productService;
    this.slotHelper = slotHelper;
  }

  @Override
  public StoreProductsInformationDto createCommand(Long storeId) {
    return getStoreProductsForCommand(storeId, null);
  }

  @Override
  public StoreProductsInformationDto updateCommand(Long storeId, Long commandId) {
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
    // Todo rajouter le creneau selectionné si modification d'une commande
    // Todo mettre a jour test unitaire
    // Date de la commande
    LocalDate refCommandDate = commandInformation.commandId() == null ? LocalDate.now() : commandInformation.commandDate();

    // Début de journée jour de commande (00h01)
    LocalDateTime startOfCommandDay = refCommandDate.atStartOfDay();

    // Fin de journnée jour de commande
    LocalDateTime endOfCommandDay = LocalDateTime.from(startOfCommandDay).with(LocalTime.MAX);

    // Recherche de l'horaire de reference pour commencer à filtrer les créneaux
    LocalDateTime refFilterTime = commandInformation.consultationDate().toLocalDate().equals(refCommandDate) ?
            commandInformation.consultationDate(): startOfCommandDay;

    // Recherche des commandes du jours
    List<CommandEntity> commandsOfTheDay  = commandRepository.findCommandsByStoreIdDate(
            startOfCommandDay,
            endOfCommandDay,
            commandInformation.storeId()
    );

    // Calcul du temps de préparation
    Integer commandPreparationTime = commandServiceHelper.calculateCommandPreparationTime(commandInformation.productsSelected());

    // Informations nécessaires pour calculer les slots disponibles
    slotHelper.setCommandPreparationTime(commandPreparationTime);
    slotHelper.setEndOfCommandDay(endOfCommandDay);
    slotHelper.setStartOfCommandDay(startOfCommandDay);
    slotHelper.setRefFilterTime(refFilterTime);

    // Renvoi une liste de slot disponible filtré par rapport aux horaires d'ouverture et les commandes déja existantes
    return  slotHelper.getStoreSlotAvailibility(commandInformation.storeId(), commandsOfTheDay);
  }

  @Override
  public RegisterCommandDto persistCommand(PersitCommandDto persitCommand) {
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
            persitCommand.selectProducts()
    );

    // Verification validité de la commande
    validateProductsSelection(commandInformation.storeId(), commandInformation.commandId(), productSelectInformation);

    // Validation du creneau de la commande
    validateSlot(commandInformation,persitCommand.selectSlotTime());

    // Code de la commande
    String commandCode = commandServiceHelper.generateCode(COMMAND_CODE_LENGTH);

    // Calcul du temps de preparation
    Integer preparationTime = commandServiceHelper.calculateCommandPreparationTime(persitCommand.selectProducts());

    // Calcul du prix de la commande
    Double commandPrice = commandServiceHelper.calculateCommandPrice(persitCommand.selectProducts());

    // Calcul du nombre de produit
    Integer numberOfProductInCommand = commandServiceHelper.calculateNumberOfProductInCommand(persitCommand.selectProducts());

    CommandEntity command = persitCommand.commandId() == null ?

      commandTransactionSession.saveCommand(
        Factory.getCommandInformationToSave(
          persitCommand,
          commandCode,
          preparationTime,
          numberOfProductInCommand,
          commandPrice
        ))
            :
      commandTransactionSession.updateCommand(
        Factory.getCommandInformationToUpdate(
          persitCommand,
          preparationTime,
          numberOfProductInCommand,
          commandPrice
        ));

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

      // Si commande existante alors vérification du storeId
      if(!command.getStore().getId().equals(storeId))
        throw new RuntimeException("Cette commande n'est pas rattaché au commerce");
    }

    // Verifier liste de produits > 0 et produit appartient au commerce
    if(productSelectInformation.productSelectList() ==null || productSelectInformation.productSelectList().size() == 0)
      throw new RuntimeException("La commande ne peut pas être vide");

    productSelectInformation.productSelectList().forEach(productWithQuantity -> {
      ProductEntity product = productService.findProduct(productWithQuantity.getProductId());
      // Vérification que le produit appartienne au commerce
      if(!product.getStore().getId().equals(storeId))
        throw new RuntimeException(String.format("Le produit %s n'est pas référencé dans ce commerce", product.getName()));

      // Vérification que le produit est disponible
      if(!product.getIsAvail())
        throw new RuntimeException(String.format("Le produit %s n'est plus disponible", product.getName()));

      // Vérification de la quantité de chaque produit
      if(productWithQuantity.getProductQuantity() < 1)
        throw new RuntimeException(String.format("Merci d'indiquer la quantité pour le produit %s", product.getName()));
    });
  }

  @Override
  public void validateSlot(CommandInformationDto commandInformation, LocalDateTime selectSlotTime) {
    // Récuperation des créneaux disponible
    List<LocalDateTime> storeAvailibiltySlots = getStoreSlotAvailibility(commandInformation);

    if(storeAvailibiltySlots
            .stream()
            .filter(storeSlot->selectSlotTime.equals(storeSlot))
            .collect(Collectors.toList())
            .size() == 0) throw new CommandException("Le créneau demandé n'est plus disponible", HttpStatus.BAD_REQUEST);
  }



}
