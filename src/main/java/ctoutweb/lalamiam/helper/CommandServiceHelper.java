package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.*;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CommandServiceHelper extends RepositoryCommonMethod {

  int CODE_COMMAND_LENGTH = 6;
  private final CommandRepository commandRepository;
  private final ProductRepository productRepository;

  private final CommandRepositoryHelper commandRepositoryHelper;

  public CommandServiceHelper(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          ProductRepository productRepository,
          CommandRepositoryHelper commandRepositoryHelper) {
    super(commandProductRepository, productRepository);
    this.commandRepository = commandRepository;
    this.productRepository = productRepository;
    this.commandRepositoryHelper = commandRepositoryHelper;
  }

  /**
   * Création d'une commande
   * @param addCommand
   * @return
   * @throws RuntimeException
   */
  public CompleteCommandDetailResponseDto addCommand(StoreEntity store, AddCommandDto addCommand) throws RuntimeException {
    // Vérification si produit appartiennent au store
    if(!store.areProductsBelongToStore (addCommand.productsInCommand() , productRepository))
      throw new RuntimeException("Le produit n'est pas rattaché au store");

    // Persistence des données
    CommandWithCalculateDetail saveCommand = commandRepositoryHelper.saveCommand(addCommand, generateCode(CODE_COMMAND_LENGTH));

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
  public UpdateProductQuantityResponseDto updateProductQuantityInCommand (UpdateProductQuantityDto updateProductQuantity)
  throws RuntimeException {

    // Vérification rattachement produit à la commande
    CommandProductEntity findProductInCommand = findProductByIdInCommand(
            updateProductQuantity.getCommandId(),
            updateProductQuantity.getProductId()
    );

    // Mise a jour de la quantité de produit
    findProductInCommand.setProductQuantity(updateProductQuantity.getProductQuantity());

    // Persistence des données
    ProductInCommandWithCalculateDetail productCommandWithCalculateDetail = commandRepositoryHelper.updateProductQuantityCommand(findProductInCommand);

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
    CommandIdWithCalculateDetail productCommandWithCalculateDetail = commandRepositoryHelper.deleteProductInCommand(
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
    CommandIdWithCalculateDetail productCommandWithCalculateDetail = commandRepositoryHelper.addProductInExistingCommand(
            addProductsInCommand
    );

    return Factory.getSimplifyCommandDetailResponse(
            productCommandWithCalculateDetail.commandId(),
            productCommandWithCalculateDetail.calculateCommandDetail());
  }

  public String generateCode(Integer wordLength) {
    Random rand = new Random();
    String str = rand.ints(48, 123)
            .filter(num->(num < 58 || num > 64) && (num < 91 || num > 96))
            .limit(wordLength)
            .mapToObj(c->(char) c).collect(StringBuffer::new , StringBuffer::append, StringBuffer::append)
            .toString();
    return str.toLowerCase();
  }

}
