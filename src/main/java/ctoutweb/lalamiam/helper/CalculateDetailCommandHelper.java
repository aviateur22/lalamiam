package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.CalculateCommandDetail;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Calcul des données de la commande
 * ex: Temps de préparation, prix, ....
 */
@Component
public class CalculateDetailCommandHelper extends RepositoryCommonMethod {

  private final CommandProductRepository commandProductRepository;
  private final ProductRepository productRepository;

  public CalculateDetailCommandHelper(CommandProductRepository commandProductRepository, ProductRepository productRepository) {
    super(commandProductRepository, productRepository);
    this.commandProductRepository = commandProductRepository;
    this.productRepository = productRepository;
  }

  /**
   * Calcule des details de la commande
   * Ex temps de preparation, prix, nombre de produit, liste des produits de la commande
   * @param optionalProductsInCommand
   * @return
   */
  public CalculateCommandDetail calculateCommandDetail(
          Optional<List<ProductWithQuantity>> optionalProductsInCommand,
          BigInteger commandId
  ) {
    // Récupération de la liste des produist de la commande
    List<ProductWithQuantity> productsInCommand = findAllProductsInCommand(optionalProductsInCommand, commandId);

    // Calcul des données sur la commande
    Integer commandPreparationTime = calculateCommandPreparationTime(productsInCommand);
    Integer commandProductCount = calculatedNumberOfProductInCommand(productsInCommand);
    Double commandPrice = calculateCommandPrice(productsInCommand);

    return Factory.getCommandDetailCalculated(
            productsInCommand,
            commandPreparationTime,
            commandProductCount,
            commandPrice);
  }

  /**
   * Liste des produits d'une commande
   * @param optionalProductsInCommand
   * @return
   */
  private List<ProductWithQuantity> findAllProductsInCommand(
          Optional<List<ProductWithQuantity>> optionalProductsInCommand,
          BigInteger commandId
  ) {
    return optionalProductsInCommand.isEmpty() ?
            findAllProductInCommand(commandId)
            : optionalProductsInCommand.get();
  }



  /**
   * Calcul le temps de préparation d'une commande
   * @return
   */
  private Integer calculateCommandPreparationTime(List<ProductWithQuantity> productsInCommand ) {
    return productsInCommand
            .stream()
            .map(productInCommand -> {
              Integer productPreparationTime = Factory.getProduct(productInCommand.getProductId())
                      .findProductById(productRepository)
                      .getPreparationTime();
              return productPreparationTime * productInCommand.getProductQuantity();
            })
            .collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Renvoie le nbre de produit dans une commande
   * @param productInCommandList
   * @return
   */
  private Integer calculatedNumberOfProductInCommand(List<ProductWithQuantity> productInCommandList) {
    return productInCommandList
            .stream()
            .map(ProductWithQuantity::getProductQuantity)
            .collect(Collectors.summingInt(Integer::intValue));
  }
  /**
   * Calcul le prix d'une commande
   * @param productsInCommand
   * @return
   * @throws RuntimeException
   */
  private Double calculateCommandPrice(List<ProductWithQuantity> productsInCommand) throws RuntimeException {
    return productsInCommand
            .stream()
            .map(productInCommand -> {
              Double productPrice = Factory.getProduct(productInCommand.getProductId())
                      .findProductPrice(productRepository);
              return  productInCommand.getProductQuantity() * productPrice;
            })
            .collect(Collectors.summingDouble(Double::doubleValue));
  }
}
