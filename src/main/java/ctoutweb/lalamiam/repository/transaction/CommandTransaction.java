package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.CalculateDetailCommandHelper;
import ctoutweb.lalamiam.model.CalculateCommandDetail;
import ctoutweb.lalamiam.model.CommandWithCalculateDetail;
import ctoutweb.lalamiam.model.CommandIdWithCalculateDetail;
import ctoutweb.lalamiam.model.ProductInCommandWithCalculateDetail;
import ctoutweb.lalamiam.model.dto.AddCommandDto;
import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Component
public class CommandTransaction extends RepositoryCommonMethod {

  private final CommandProductRepository commandProductRepository;
  private final CommandRepository commandRepository;
  private final CalculateDetailCommandHelper calculateDetailCommandHelper;

  public CommandTransaction(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          ProductRepository productRepository,
          CalculateDetailCommandHelper calculateDetailCommandHelper
  ) {
    super(commandProductRepository, productRepository);
    this.commandProductRepository = commandProductRepository;
    this.commandRepository = commandRepository;
    this.calculateDetailCommandHelper = calculateDetailCommandHelper;
  }

  /**
   *
   * @param addCommand
   * @param commancCode
   * @return
   */
  @Transactional
  public CommandWithCalculateDetail saveCommand(
          AddCommandDto addCommand,
          String commancCode
  ) {

    // Calcul des données de la commande
    CalculateCommandDetail commandDetailCalculated = calculateDetailCommandHelper.calculateCommandDetail(
            Optional.of(addCommand.productsInCommand()),
            null
    );

    // Ajout Commande
    CommandEntity saveCommand = commandRepository.save(
            Factory.getCommand(
                    commandDetailCalculated,
                    commancCode,
                    addCommand.slotTime(),
                    addCommand.storeId(),
                    addCommand.clientPhone()
            )
    );

    // Enregistrement des produits
    addCommand.productsInCommand()
            .stream()
            .forEach(product->{
              CommandProductEntity addCommandProduct = Factory.getCommandProduct(saveCommand.getId(), product.getProductId(), product.getProductQuantity());
              commandProductRepository.save(addCommandProduct);
            });

    return Factory.getCommandWithCalculatedDetail(saveCommand, commandDetailCalculated);
  }

  /**
   * Enregistrement d'une nouvelle commande
   * @param productToBeUpdated
   * @return
   */
  @Transactional
  public ProductInCommandWithCalculateDetail updateProductQuantityCommand(CommandProductEntity productToBeUpdated) {
    // Sauvegarde des informations produit dans la commande
    CommandProductEntity productUpdated =  commandProductRepository.save(productToBeUpdated);

    // Calcul des données de la commande
    CalculateCommandDetail commandDetailCalculated = calculateDetailCommandHelper.calculateCommandDetail(
            Optional.ofNullable(null),
            productUpdated.getCommand().getId()
    );

    // sauvegarde des données de la commande
    updateCalculatedCommandDetail(productUpdated.getCommand().getId(), commandDetailCalculated);

    return Factory.getProductQuantityWithCalculatedDetail(productUpdated, commandDetailCalculated);
  }

  @Transactional
  public CommandIdWithCalculateDetail deleteProductInCommand(BigInteger commandId, BigInteger productId) {

    // Suppression du produit
    commandProductRepository.deleteProductByCommandIdAndProductId(commandId, productId);

    // Calcul des données de la commande
    CalculateCommandDetail commandDetailCalculated = calculateDetailCommandHelper.calculateCommandDetail(
            Optional.ofNullable(null),
            commandId
    );

    // sauvegarde des données de la commande
    updateCalculatedCommandDetail(commandId, commandDetailCalculated);

    return Factory.getProductCommandWithCalculatedDetail(commandId, commandDetailCalculated);
  }

  @Transactional
  public CommandIdWithCalculateDetail addProductInExistingCommand(AddProductsInCommandDto addProductsInCommand) {

    final Integer ADD_PRODUCT_QUANTITY = 1;

    var listProductWrapper = new Object() {
      List<ProductWithQuantity> productsInCommand = findAllProductInCommand(addProductsInCommand.commandId());
    };

    addProductsInCommand.productIdList().stream().forEach(productId-> {
      findProductInCommandList(productId, listProductWrapper.productsInCommand)
              .ifPresentOrElse((value)->{
                // Mise a jour d'un produit
                CommandProductEntity updateProduct = findProductByIdInCommand(
                        addProductsInCommand.commandId(),
                        productId
                ).orElseThrow(()->new RuntimeException(""));
                updateProduct.setProductQuantity(updateProduct.getProductQuantity() + ADD_PRODUCT_QUANTITY);
                updateProductQuantityCommand(updateProduct);
              },
              ()->{

                // Ajout d'un produit
                CommandProductEntity addProduct = Factory.getCommandProduct(
                        addProductsInCommand.commandId(),
                        productId,
                        ADD_PRODUCT_QUANTITY
                );
                commandProductRepository.save(addProduct);
              });
    });

    // Calcul des données de la commande
    CalculateCommandDetail commandDetailCalculated = calculateDetailCommandHelper.calculateCommandDetail(
            Optional.ofNullable(null),
            addProductsInCommand.commandId()
    );

    // sauvegarde des données de la commande
    updateCalculatedCommandDetail(addProductsInCommand.commandId(), commandDetailCalculated);

    return Factory.getProductCommandWithCalculatedDetail(addProductsInCommand.commandId(), commandDetailCalculated);
  }

  /**
   * Mise a jour des détails d'une commande
   * @param commandId
   * @return
   */
  @Transactional
  private CommandEntity updateCalculatedCommandDetail(
          BigInteger commandId,
          CalculateCommandDetail calculateCommandDetail
  ) throws RuntimeException {
    // Recherche de la commande pour mise a jour
    CommandEntity commmandToBeUpdated = commandRepository
            .findById(commandId)
            .orElseThrow(() -> new RuntimeException("La commande à mettre à jour n'existe pas"));

    commmandToBeUpdated.setCommandPrice(calculateCommandDetail.commandPrice());
    commmandToBeUpdated.setProductQuantity(calculateCommandDetail.numberOProductInCommand());
    commmandToBeUpdated.setPreparationTime(calculateCommandDetail.commandPreparationTime());

    // Mise a jour des données de la commande
    return commandRepository.save(commmandToBeUpdated);
  }

  /**
   * Récupération d'un produit dans une commande
   * @param productId
   * @return
   */
  public Optional<ProductWithQuantity> findProductInCommandList (
          BigInteger productId,
          List<ProductWithQuantity> productsInCommand) {
    return productsInCommand
            .stream()
            .filter(product->product.getProductId().equals(productId))
            .findFirst();
  }


}
