package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.*;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.repository.transaction.CommandTransaction;
import ctoutweb.lalamiam.repository.transaction.RepositoryCommonMethod;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommandServiceHelper extends RepositoryCommonMethod {

  int CODE_COMMAND_LENGTH = 6;
  private final CommandRepository commandRepository;
  private final ProductRepository productRepository;
  private final CommandProductRepository commandProductRepository;
  private final ScheduleRepository scheduleRepository;
  private final CommandTransaction commandTransaction;

  public CommandServiceHelper(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          ProductRepository productRepository,
          ScheduleRepository scheduleRepository,
          CommandTransaction commandRepositoryHelper
  ) {
    super(commandProductRepository, productRepository);
    this.commandRepository = commandRepository;
    this.productRepository = productRepository;
    this.commandProductRepository = commandProductRepository;
    this.scheduleRepository = scheduleRepository;

    this.commandTransaction = commandRepositoryHelper;
  }

  /**
   * Création d'une commande
   * @param addCommand
   * @return
   * @throws RuntimeException
   */
  public CompleteCommandDetailResponseDto addCommand(StoreEntity store, AddCommandDto addCommand) throws RuntimeException {
    // Persistence des données
    CommandWithCalculateDetail saveCommand = commandTransaction.saveCommand(addCommand, generateCode(CODE_COMMAND_LENGTH));

    return Factory.getCompleteCommandDetailDto(
            saveCommand.command(),
            saveCommand.calculateCommandDetail()
    );
  }

  /**
   * Mise a jour de la quantité d'un produit dans une commande
   * @return
   * @throws RuntimeException
   */
  public UpdateProductQuantityResponseDto updateProductQuantityInCommand(UpdateProductQuantityDto updateProductQuantity) throws RuntimeException {
    // Récupération du produit a modifier
    CommandProductEntity findProductInCommand = commandProductRepository.findOneProductByCommandIdProductId(
            updateProductQuantity.getCommandId(),
            updateProductQuantity.getProductId()
    ).orElseThrow(()-> new RuntimeException("Le produit à modifier n'est pas associé à la commande"));

    // Mise a jour de la quantité de produit
    findProductInCommand.setProductQuantity(updateProductQuantity.getProductQuantity());

    // Persistence des données
    ProductInCommandWithCalculateDetail productCommandWithCalculateDetail = commandTransaction.updateProductQuantityCommand(findProductInCommand);

    // Récupération liste produits avec quantité
    ProductWithQuantity productUpdatedWithQuantity = Factory.getProductWithQuantity(productCommandWithCalculateDetail.productInCommand());

    return Factory.getUpdateProductQuantityResponse(
            productCommandWithCalculateDetail.productInCommand().getCommand().getId(),
            productUpdatedWithQuantity,
            productCommandWithCalculateDetail.calculateCommandDetail()
    );
  }

  /**
   * Suppression produit dans 1 commande
   * @param deleteProductInCommand - Produit a supprimmé
   * @return CommandDetailDto
   */
  public SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand) {

    // Récuperation des données de la commande
    commandRepository
            .findById(deleteProductInCommand.commandId())
            .orElseThrow(()->new RuntimeException("La commande n'est pas trouvée"));

    // Persistence des données
    CommandIdWithCalculateDetail productCommandWithCalculateDetail = commandTransaction.deleteProductInCommand(
            deleteProductInCommand.commandId(),
            deleteProductInCommand.productId()
    );

    return Factory.getSimplifyCommandDetailResponse(
            productCommandWithCalculateDetail.commandId(),
            productCommandWithCalculateDetail.calculateCommandDetail());
  }

  /**
   * Ajout d'un ou plusieurs produits dans la commande
   * @param addProductsInCommand
   * @return CommandDetailDto
   */
  public SimplifyCommandDetailResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand) {
    // Persistence des données
    CommandIdWithCalculateDetail productCommandWithCalculateDetail = commandTransaction.addProductInExistingCommand(
            addProductsInCommand
    );

    return Factory.getSimplifyCommandDetailResponse(
            productCommandWithCalculateDetail.commandId(),
            productCommandWithCalculateDetail.calculateCommandDetail());
  }

  /**
   * Recherche des crééneaux disponible
   * @param START_OF_COMMAND_DAY
   * @param END_OF_COMMAND_DAY
   * @param REF_FILTER_TIME
   * @param commandPreparationTime
   * @param store
   * @return List<LocalDateTime>
   */
  public List<LocalDateTime> findListOfSlotAvailable(
          final LocalDateTime START_OF_COMMAND_DAY,
          final LocalDateTime END_OF_COMMAND_DAY,
          final LocalDateTime REF_FILTER_TIME,
          Integer commandPreparationTime,
          StoreEntity store
  ) {
    // Rechechre des commandes en cours
    var commands = commandRepository.findAllBusySlotByStoreId(START_OF_COMMAND_DAY, END_OF_COMMAND_DAY, store.getId())
            .stream().map(CommandEntity::getSlotTime).collect(Collectors.toList());

    // Recherche des horaires du commerce
    var storeSchedules = scheduleRepository.findAllByStore(store);

    final Integer ITERATION_PER_DAY = calculateNumberOfCommandSlotForOneDay(store);

    List<LocalDateTime> slotAvailibilityInDay = Stream
            .iterate(START_OF_COMMAND_DAY, dateTime-> dateTime.plusMinutes(store.getFrequenceSlotTime()))
            .limit(ITERATION_PER_DAY)
            // Retir les slots avant REF_FILTER_TIME
            .filter(slot->slot.isAfter(REF_FILTER_TIME.plusMinutes(commandPreparationTime)))

            // Retire les slots déja pris par d'autres commandes
            .filter(slot->!commands.contains(slot))

            // Retire tous les slots qui ne sont pas dans les horaires du commerce
            .filter(slot->storeSchedules
                    .stream()
                    .anyMatch(schedule->CommonFunction.isSlotInStoreSchedule(slot,
                            schedule,
                            START_OF_COMMAND_DAY.toLocalDate(),
                            END_OF_COMMAND_DAY.toLocalDate(),
                            commandPreparationTime
                    )))
            .collect(Collectors.toList());

    return slotAvailibilityInDay;
  }
  /**
   * Génération d'un code aléatoire
   * @param wordLength
   * @return String
   */
  public String generateCode(Integer wordLength) {
    Random rand = new Random();
    String str = rand.ints(48, 123)
            .filter(num->(num < 58 || num > 64) && (num < 91 || num > 96))
            .limit(wordLength)
            .mapToObj(c->(char) c).collect(StringBuffer::new , StringBuffer::append, StringBuffer::append)
            .toString();
    return str.toLowerCase();
  }

  /**
   * Vérification si une liste de produits appartient à store
   * @param productsId
   * @param storeId
   * @return boolean
   */
  public boolean areProductsBelongToStore(List<BigInteger> productsId, BigInteger storeId) {
    return productsId
            .stream()
            .map(productId-> isProductBelongToStore(productId, storeId))
            .allMatch(Boolean::booleanValue);
  }

  /**
   * Vérification si un produit appartient à store
   * @param productId
   * @param storeId
   * @return boolean
   */
  public boolean isProductBelongToStore(BigInteger productId, BigInteger storeId) {
    return Factory.getProduct(productId)
            .findProductById(productRepository)
            .getStore()
            .getId().equals(storeId);
  }

  /**
   * Vérification d'une commande a modifiéer
   * @param commandId
   * @param productsId
   * @param storeId
   */
  public void verifyUpdatedCommand(BigInteger commandId, List<BigInteger> productsId, BigInteger storeId) {
    // Reference horaire
    final LocalDateTime TIME_REFERERENCE = LocalDateTime.now();

    // Vérification existance commande si mise a jour de la commande
    CommandEntity updatedCommand = commandRepository
            .findById(commandId)
            .orElseThrow(()->new RuntimeException("La commande n'est pas trouvée"));

    // Vérification si le produit appartient au commerce
    if(!areProductsBelongToStore(productsId, storeId))
      throw new RuntimeException ("Certains produits à modifier ne sont pas rattachés au commerce");

    // Vérification de l'heure de commande
    if(updatedCommand.getSlotTime().isBefore(TIME_REFERERENCE)) throw new RuntimeException("La commande ne peut pas être dans le passée");

    // Vérification rattachement produit à la commande
    if(!areProductsInCommand(commandId, productsId))
      throw new RuntimeException("Certains produits à modifier ne sont pas rattachés à la commande");
  }

  /**
   * Vérification d'une commande a créer
   * @param addCommand
   */
  public void verifyCreatedCommand(AddCommandDto addCommand) {
    // Reference horaire
    final LocalDateTime TIME_REFERERENCE = LocalDateTime.now();

    if(CommonFunction.isNullOrEmpty(addCommand.clientPhone())) throw new RuntimeException("Le téléphone client est obligatoire");

    if(addCommand.slotTime().isBefore(TIME_REFERERENCE)) throw new RuntimeException("La commande ne peut pas être dans le passée");

    if(addCommand.productsInCommand() == null || addCommand.productsInCommand().isEmpty()) throw new RuntimeException("La commande ne peut pas être vide");

    // Vérification si le produit appartient au commerce
    List<BigInteger> productsId = addCommand.productsInCommand().stream().map(ProductWithQuantity::getProductId).collect(Collectors.toList());
    if(!areProductsBelongToStore(productsId, addCommand.storeId()))
      throw new RuntimeException ("Certains produits à ajouter ne sont pas rattachés au commerce");
  }

  /**
   * Calcul du nombre de créneaux pour commande disponable sur 24h pour 1 magasin
   * Ne tient pas compte des commandes déja présentes
   * @param store
   * @return Integer
   */
  public Integer calculateNumberOfCommandSlotForOneDay(StoreEntity store) {
    // Nombre de minute dans 1h
    final int MINUTES_IN_HOUR = 60;

    // Nombre d' heure dans 1 journée
    final int HOUR_IN_DAY = 24;

    // Nombre d'itération de commande par heure
    final int ITERATION_PER_HOUR = MINUTES_IN_HOUR / store.getFrequenceSlotTime();

    // Nombre d'iteration pour 24h
    return ITERATION_PER_HOUR * HOUR_IN_DAY;
  }



}
