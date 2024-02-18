package ctoutweb.lalamiam.service.serviceImpl;

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
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
  public StoreProductsInformationDto createCommand(BigInteger storeId) {
    return getStoreProductsForCommand(storeId, null);
  }

  @Override
  public StoreProductsInformationDto updateCommand(BigInteger storeId, BigInteger commandId) {
    return getStoreProductsForCommand(storeId, commandId);
  }

  @Override
  public RegisterCommandDto getCommand(BigInteger storeId, BigInteger commandId) {
    // Récupération de la commande
    CommandEntity command = commandTransactionSession.getCommand(commandId);

    // Si commande existante alors vérification du storeIs
    if(command == null)
      throw new RuntimeException("Cette commande n'existe pas");

    // Si commande existante alors vérification du storeIs
    if(command != null && !command.getStore().getId().equals(storeId))
      throw new RuntimeException("Cette commande n'est pas rattaché au commerce");

    // Recherche d'une commande
    return commandServiceHelper.findRegisterCommandInformation(storeId, command);
  }

  @Override
  public StoreProductsInformationDto getStoreProductsForCommand(BigInteger storeId, BigInteger commandId) {

    // Récupération de la commande
    CommandEntity command = commandTransactionSession.getCommand(commandId);

    // Si commande existante alors vérification du storeIs
    if(command != null && !command.getStore().getId().equals(storeId))
      throw new RuntimeException("Cette commande n'est pas rattaché au commerce");


    // Recherche d'une commande
    RegisterCommandDto registerCommand = commandServiceHelper.findRegisterCommandInformation(storeId, command);

    // Recherche des produits du commerce avec ajout des quantité de présente dans la commande
    List<ProductWithQuantityDto> findStoreProductsWithQuantity = commandServiceHelper.getStoreProductsWithCommandQuantity(storeId, registerCommand);

    return Factory.getCommandInformationDto(registerCommand, findStoreProductsWithQuantity);
  }

  @Override
  public void validateProductsSelection(BigInteger storeId, BigInteger commandId, ProductSelectInformationDto productSelectInformation) {
    // Todo implementer les tests unitaires
    if(storeId == null)  throw new RuntimeException("Identifiant commerce obligatoire");

    // N°Tel
    if(productSelectInformation.clientPhone() == null) throw new RuntimeException("Le numéro de téléphone est obligatoire");

    CommandEntity command = null;
    if(commandId != null) command = commandTransactionSession.getCommand(commandId);

    // Si commande existante alors vérification du storeId
    if(command != null && !command.getStore().getId().equals(storeId))
      throw new RuntimeException("Cette commande n'est pas rattaché au commerce");

    // Verifier liste de produits > 0 et produit appartient au commerce
    if(productSelectInformation.productSelectList().size() == 0)
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
  public List<LocalDateTime> getStoreSlotAvailibility (CommandInformationDto commandInformation) {

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
  public void validateSlot() {

  }

  @Override
  public RegisterCommandDto persistCommand() {
    return null;
  }


}
