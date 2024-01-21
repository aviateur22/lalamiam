package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import java.math.BigInteger;
import java.util.List;
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
  public CommandProductEntity findProductByIdInCommand(BigInteger commandId, BigInteger productId) throws RuntimeException {
    return commandProductRepository
            .findOneProductByCommandIdProductId(commandId, productId)
            .orElseThrow(() -> new RuntimeException("Le produit n'est pas dans la commande"));
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
