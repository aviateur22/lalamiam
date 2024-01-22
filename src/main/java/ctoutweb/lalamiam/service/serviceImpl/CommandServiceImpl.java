package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.CommandServiceHelper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public SimplifyCommandDetailResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand) {

    // Verification des données a modifier
    verifyRequest(
            addProductsInCommand.storeId(),
            addProductsInCommand.commandId(),
            addProductsInCommand.productIdList());

   return commandServiceHelper.addProductsInCommand(addProductsInCommand);
  }

  @Override
  public List<LocalDateTime> findAllSlotAvailable(FindSlotTimeDto findSlotTime) {
// TODO une commande ne peut pas être dans le passé
    // Récuperation des données du commerce
    StoreEntity store = storeRepository.findById(findSlotTime.getStoreId()).orElseThrow();

    // Heure de la requête
    final LocalDateTime REQUEST_TIME = LocalDateTime.now().plusDays(1);

    // Date de la commande
    final LocalDate COMMAND_DATE = findSlotTime.getCommandRequestedDate();

   // Nombre de minute dans 1h
    final int MINUTES_IN_HOUR = 60;

    // Nombre d' heure dans 1 journée
    final int HOUR_IN_DAY = 24;

    // Nombre d'itération de commande par heure
    final int ITERATION_PER_HOUR = MINUTES_IN_HOUR / store.getFrequenceSlotTime();

    // Nombre d'iteration pour 24h
    final int ITERATION_PER_DAY = ITERATION_PER_HOUR * HOUR_IN_DAY;

    // Heure début journée du jour de la commande
    final LocalDateTime START_OF_COMMAND_DAY = LocalDateTime.of(
            COMMAND_DATE.getYear(),
            COMMAND_DATE.getMonth(),
            COMMAND_DATE.getDayOfMonth(),
            0,
            0,
            00
    );

    // Heure fin de journée du jour de commande
    final LocalDateTime END_OF_COMMAND_DAY = LocalDateTime.from(START_OF_COMMAND_DAY).with(LocalTime.MAX);

    // Reférence pour filtrage des slot diponible
    final LocalDateTime REF_FILTER_TIME =  COMMAND_DATE.getDayOfYear() == REQUEST_TIME.getDayOfYear() ?
            REQUEST_TIME : START_OF_COMMAND_DAY
            ;

    List<LocalDateTime> slotTimeInDay = Stream
            .iterate(START_OF_COMMAND_DAY, dateTime-> dateTime.plusMinutes(store.getFrequenceSlotTime()))
            .limit(ITERATION_PER_DAY)
            .filter(slot->slot.isAfter(REF_FILTER_TIME.plusMinutes(findSlotTime.getCommandPreparationTime())))
            .collect(Collectors.toList());

    List<LocalDateTime> listOfSlotTimeAvailable = new ArrayList<>();


    return null;
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
