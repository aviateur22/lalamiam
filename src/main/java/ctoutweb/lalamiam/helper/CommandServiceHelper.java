package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.AddCookSchema;
import ctoutweb.lalamiam.model.schema.ProductInCommand;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CookRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandServiceHelper {

  private final CookRepository cookRepository;
  private final CommandRepository commandRepository;
  private final ProductRepository productRepository;

  public CommandServiceHelper(
          CookRepository cookRepository,
          CommandRepository commandRepository,
          ProductRepository productRepository) {
    this.cookRepository = cookRepository;
    this.commandRepository = commandRepository;
    this.productRepository = productRepository;
  }
  public CommandEntity addCommand(AddCommandSchema addCommandSchema) throws RuntimeException {

    // Récupération liste des produits
    List<ProductEntity> products = findProductListById(addCommandSchema.productsInCommand()
            .stream()
            .map(ProductInCommand::productId)
            .collect(Collectors.toList())
    );

    // Vérification si les produits appartiennent au Store
    if(!isProductListBelongToStore(addCommandSchema.productsInCommand(), addCommandSchema.storeId()))
      throw new RuntimeException("Le produit n'est pas rattaché au store");

    // Prix de la commande
    Double commandPrice = calculateCommandPrice(products);

    // Temps de preparation
    Integer commandPrepartionTime = calculatCommandPreparationTime(products);

    // Ajout Commande
    CommandEntity addCommand = commandRepository.save(
            CommandEntityBuilder
                    .aCommandEntity()
                    .withClientPhone(addCommandSchema.clientPhone())
                    .withOrderPrice(commandPrice)
                    .withSlotTime(addCommandSchema.slotTime())
                    .withPreparationTime(commandPrepartionTime)
                    .build());

    // Enregistrement des produits
    addCommandSchema.productsInCommand()
            .stream()
            .forEach(productInCommand->{
              AddCookSchema addCookSchema = new AddCookSchema(
                      productInCommand.productId(),
                      addCommand.getId(),
                      addCommandSchema.storeId(),
                      productInCommand.productQuantity());
              CookEntity addCook = cookRepository.save(new CookEntity(addCookSchema));
    });

    return addCommand;
  }

  public Double calculateCommandPrice(List<ProductEntity> productList) {
    // Calcul du prix de la commande
    return productList.stream()
            .map(ProductEntity::getPrice)
            .collect(Collectors.summingDouble(Double::doubleValue));
  }

  public Integer calculatCommandPreparationTime(List<ProductEntity> productList) {
    return productList.stream()
            .map(ProductEntity::getPreparationTime)
            .collect(Collectors.summingInt(Integer::intValue));
  }

  public List<ProductEntity> findProductListById(List<BigInteger> productIdList) {
    return productIdList.stream()
            .map(id->{
              return productRepository.findById(id).orElseThrow(()->new RuntimeException("Le produit n'existe pas"));
            })
            .collect(Collectors.toList());
  }

  public boolean isProductListBelongToStore(List<ProductInCommand> productInCommandList, BigInteger storeId) {
    return productInCommandList
            .stream()
            .map(productInCommand-> {
              ProductEntity product = productRepository
                      .findById(productInCommand.productId())
                      .orElseThrow(()->new RuntimeException("Le produit n'existe pas"));
              return product.getStore().getId().equals(storeId);

    }).allMatch(Boolean::booleanValue);
  }

}
