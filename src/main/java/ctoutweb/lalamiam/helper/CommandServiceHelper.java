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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class CommandServiceHelper extends RepositoryCommonMethod {

  int CODE_COMMAND_LENGTH = 6;
  private final CommandRepository commandRepository;
  private final ProductRepository productRepository;
  private final CommandProductRepository commandProductRepository;
  private final StoreDayScheduleRepository storeWeekDayRepository;
  private final CommandTransaction commandTransaction;

  public CommandServiceHelper(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          ProductRepository productRepository,
          StoreDayScheduleRepository storeWeekDayRepository,
          CommandTransaction commandRepositoryHelper
  ) {
    super(commandProductRepository, productRepository);
    this.commandRepository = commandRepository;
    this.productRepository = productRepository;
    this.commandProductRepository = commandProductRepository;
    this.storeWeekDayRepository = storeWeekDayRepository;
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

}
