package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.repository.transaction.RepositoryCommonMethod;
import ctoutweb.lalamiam.service.CommandService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
public class CommandServiceImpl extends RepositoryCommonMethod implements CommandService {
  private final CommandServiceHelper commandServiceHelper;
  private final StoreRepository storeRepository;
  public CommandServiceImpl (
          CommandServiceHelper commandServiceHelper,
          StoreRepository storeRepository,
          ProductRepository productRepository,
          CommandProductRepository commandProductRepository) {
    super(commandProductRepository, productRepository);
    this.commandServiceHelper = commandServiceHelper;
    this.storeRepository = storeRepository;
  }

  @Override
  public CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommand) {
    StoreEntity store = Factory.getStore(addCommand.storeId());

    // Verification des données de la commande
    verifyRequest(addCommand);

    // Calcul temps de prezpartion
    return commandServiceHelper.addCommand(store, addCommand);
  }

  @Override
  @Transactional
  public UpdateProductQuantityResponseDto updateProductQuantityInCommand(
          UpdateProductQuantityDto updateProductCommand
  ) throws RuntimeException {

    // Vérification que la nouvelle quantité soit positif
    if(updateProductCommand.getProductQuantity() <= 0)
      updateProductCommand.setProductQuantity(1);

    // Verification des données a modifier
    verifyRequest(
            updateProductCommand.getStoreId(),
            updateProductCommand.getCommandId(),
            Arrays.asList(updateProductCommand.getProductId())
    );

    return commandServiceHelper.updateProductQuantityInCommand(updateProductCommand);
  }

  @Override
  public SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand) {
    // Verification des données a modifier
    verifyRequest(
            deleteProductInCommand.storeId(),
            deleteProductInCommand.commandId(),
            Arrays.asList(deleteProductInCommand.productId()));

    return commandServiceHelper.deleteProductInCommand(deleteProductInCommand);
  }

  @Override
  public AddProductsInCommandResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand) {
    // Vérification existence du commerce
    findStore(addProductsInCommand.storeId());

    // Vérification du contenu des modifications
    commandServiceHelper.verifyCommandsWithNewProducts(
            addProductsInCommand.commandId(),
            addProductsInCommand.productWithQuantityList().stream().map(ProductWithQuantity::getProductId).toList(),
            addProductsInCommand.storeId()
    );

   return commandServiceHelper.addProductsInCommand(addProductsInCommand);
  }

  @Override
  public List<LocalDateTime> findAllSlotAvailable(FindListOfSlotTimeAvailableDto findListOfSlotTime) {
// TODO une commande ne peut pas être dans le passé
    // Récuperation des données du commerce
    StoreEntity store =  storeRepository.findById(findListOfSlotTime.getStoreId()).orElseThrow();

    // Date de la commande
    final LocalDate COMMAND_DATE = findListOfSlotTime.getCommandDate();

    // Heure début journée du jour de la commande
    final LocalDateTime START_OF_COMMAND_DAY = LocalDateTime.of(
            COMMAND_DATE.getYear(),
            COMMAND_DATE.getMonth(),
            COMMAND_DATE.getDayOfMonth(),
            0,
            0,
            0
    );

    // Heure fin de journée du jour de commande
    final LocalDateTime END_OF_COMMAND_DAY = LocalDateTime.from(START_OF_COMMAND_DAY).with(LocalTime.MAX);

    // Reférence pour filtrage des slot diponible
    final LocalDateTime REF_FILTER_TIME =
            START_OF_COMMAND_DAY.getDayOfYear() == findListOfSlotTime.getSlotConslutationDate().getDayOfYear() ?
                    findListOfSlotTime.getSlotConslutationDate() : START_OF_COMMAND_DAY;

    return commandServiceHelper
            .findListOfSlotAvailable(
                    START_OF_COMMAND_DAY,
                    END_OF_COMMAND_DAY,
                    REF_FILTER_TIME,
                    findListOfSlotTime.getCommandPreparationTime(),
                    store
            );
  }

  /**
   * Vérification Commade
   */
  public void verifyRequest(BigInteger storeId, BigInteger commandId, List<BigInteger> productsId) {
    // Vérification existence du commerce
    findStore(storeId);

    // Vérification du contenu des modifications
    commandServiceHelper.verifyUpdatedCommand(commandId, productsId, storeId);
  }

  /**
   * Vérification Commade
   * @param addCommand
   */
  public void verifyRequest(AddCommandDto addCommand) {
    // Vérification existence du commerce
    findStore(addCommand.storeId());

    // Vérification du contenu de la nouvelle commande
    commandServiceHelper.verifyCreatedCommand(addCommand);
  }

  /**
   * Recherche d'un commerce
   * @param storeId
   */
  public void findStore(BigInteger storeId) {
    // Vérification du Store
    StoreEntity store = storeRepository
            .findById(storeId)
            .orElseThrow(()->new RuntimeException("Le commerce n'existe pas"));
  }

}
