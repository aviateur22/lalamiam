package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositoryCommonMethod {

  private final CommandProductRepository commandProductRepository;
  private final ProductRepository productRepository;

  public RepositoryCommonMethod(CommandProductRepository commandProductRepository, ProductRepository productRepository) {
    this.commandProductRepository = commandProductRepository;
    this.productRepository = productRepository;
  }

  /**
   * Cherche un produit dans la commande
   * @param commandId
   * @param productId
   * @return CommandProductEntity || Throw
   */
  public Optional<CommandProductEntity> findProductByIdInCommand(BigInteger commandId, BigInteger productId) {
    return commandProductRepository.findOneProductByCommandIdProductId(commandId, productId);
  }

  /**
   * Vérification que tous les produits d'une liste sont présent dans une commande
   * @param commandId
   * @param productsId
   * @return CommandProductEntity || Throw
   */
  public boolean areProductsInCommand(BigInteger commandId, List<BigInteger> productsId) {
    return productsId
            .stream()
            .map(productId->findProductByIdInCommand(commandId, productId))
            .allMatch(Optional::isPresent);
  }

  /**
   * Création Liste procutInCommand pour une commande
   * @return List<ProductInCommand>
   * @throws RuntimeException
   */
  public List<ProductWithQuantity> findAllProductInCommand(BigInteger commandId) throws RuntimeException {

    return commandProductRepository.findProductsByCommandId(commandId).stream().map(productCommand-> {
      Integer productQuantity = productCommand.getProductQuantity();
      BigInteger productId = productCommand.getProduct().getId();

      ProductEntity product = Factory.getProduct(productId).findProductById(productRepository);

      return new ProductWithQuantity(product.getId(), productQuantity);
    }).collect(Collectors.toList());
  }
}
