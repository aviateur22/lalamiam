package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.model.dto.CommandInformationDto;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.NewCommandService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewCommandServiceImp implements NewCommandService {
  private final NewCommandServiceHelper commandServiceHelper;
  private final CommandTransactionSession commandTransactionSession;

  public NewCommandServiceImp(
          NewCommandServiceHelper newCommandServiceHelper,
          CommandTransactionSession commandTransactionSession
  ) {
    this.commandServiceHelper = newCommandServiceHelper;
    this.commandTransactionSession = commandTransactionSession;
  }

  @Override
  public CommandInformationDto createCommand(BigInteger storeId) {
    return getStoreProductsForCommand(storeId, null);
  }

  @Override
  public CommandInformationDto updateCommand(BigInteger storeId, BigInteger commandId) {
    return getStoreProductsForCommand(storeId, commandId);
  }

  @Override
  public RegisterCommandDto getCommand(BigInteger storeId, BigInteger commandId) {
    // TODO implementer test
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
  public CommandInformationDto getStoreProductsForCommand(BigInteger storeId, BigInteger commandId) {

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
  public void validateProductsSelection() {}

  @Override
  public List<LocalDateTime> displayStoreSlotAvailibility() {
    return null;
  }

  @Override
  public void validateSlot() {

  }

  @Override
  public RegisterCommandDto persistCommand() {
    return null;
  }


}
