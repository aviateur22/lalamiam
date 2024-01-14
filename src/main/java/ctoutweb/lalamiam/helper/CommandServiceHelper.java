package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.model.dto.AddCommandDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.AddCookSchema;
import ctoutweb.lalamiam.model.schema.ProductInCommand;
import ctoutweb.lalamiam.model.schema.UpdateProductQuantityInCommandSchema;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CookRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
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

  /**
   * Création d'une commande
   * @param addCommandSchema
   * @return
   * @throws RuntimeException
   */
  public AddCommandDto addCommand(AddCommandSchema addCommandSchema) throws RuntimeException {

    // Vérification si les produits appartiennent au Store
    if(!isProductListBelongToStore(addCommandSchema.productsInCommand(), addCommandSchema.storeId()))
      throw new RuntimeException("Le produit n'est pas rattaché au store");

    // Prix de la commande
    Double commandPrice = calculateCommandPrice(addCommandSchema.productsInCommand());

    // Temps de preparation
    Integer commandPrepartionTime = calculateCommandPreparationTime(addCommandSchema.productsInCommand());

    // Génération CodeCommande
    String commandCode = generateCode(6);

    // Ajout Commande
    CommandEntity addCommand = commandRepository.save(
            CommandEntityBuilder
                    .aCommandEntity()
                    .withClientPhone(addCommandSchema.clientPhone())
                    .withOrderPrice(commandPrice)
                    .withSlotTime(addCommandSchema.slotTime())
                    .withPreparationTime(commandPrepartionTime)
                    .withCommandCode(commandCode)
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

    // Récuperatation des produits de la commande
    List<ProductInCommand> productInCommandList = findAllProductInCommand(addCommandSchema.storeId(), addCommand.getId());

    // Calcul quantité produit
    Integer numberOfProductInCommande = calculatedNumberOfProductInCommand(productInCommandList);


    return new AddCommandDto(
            addCommand.getId(),
            productInCommandList,
            commandPrepartionTime,
            numberOfProductInCommande,
            commandPrice,
            addCommand.getClientPhone(),
            commandCode,
            addCommand.getSlotTime());
  }

  /**
   * Mise a jour de la quantité d'un produit dans une commande
   * @param productCook
   * @param updateProductQuantityInCommand
   * @param updatedCommand
   * @return
   * @throws RuntimeException
   */
  public UpdateProductQuantityInCommandDto updateProductQuantityInCommand (
          CookEntity productCook,
          UpdateProductQuantityInCommandSchema updateProductQuantityInCommand,
          CommandEntity updatedCommand
  ) throws RuntimeException {

    // Recherche de la liste des produits en commande
    List<ProductInCommand> productInCommandList = findAllProductInCommand(
            updateProductQuantityInCommand.getStoreId(),
            updateProductQuantityInCommand.getCommandId());

    // Calcul du nouveau prix de la commande
    Double commandPrice = calculateCommandPrice(productInCommandList);

    // Calcul du nouveau du temps de preparation de la commande
    Integer commandPrepartionTime = calculateCommandPreparationTime(productInCommandList);

    // Calcul du nouveau du nombre de produits dans la commande
    Integer totalProductInCommand = calculatedNumberOfProductInCommand(productInCommandList);

    // Enregistrement de la quantité du produit
    productCook.setProductQuantity(updateProductQuantityInCommand.getProductQuantity());
    CookEntity productUpdate = cookRepository.save(productCook);

    // Enregistrement des nouvelles donnes de la commandes
    updatedCommand.setOrderPrice(commandPrice);
    updatedCommand.setPreparationTime(commandPrepartionTime);
    commandRepository.save(updatedCommand);

    return new UpdateProductQuantityInCommandDto(
            updatedCommand.getId(),
            new ProductInCommand(productUpdate.getProduct().getId(), productUpdate.getProductQuantity()),
            commandPrepartionTime,
            totalProductInCommand,
            commandPrice);

  }

  /**
   * Calcul le prix d'une commande
   * @param productInCommandList
   * @return
   */
  public Double calculateCommandPrice(List<ProductInCommand> productInCommandList) throws RuntimeException {
    // Calcul du prix de la commande
    return productInCommandList
            .stream()
            .map(productInCommand -> {
              double productPrice = productRepository
                      .findById(productInCommand.productId())
                      .orElseThrow(()-> new RuntimeException("Le produit n'existe pas"))
                      .getPrice();
              return productInCommand.productQuantity() * productPrice;
            })
            .collect(Collectors.summingDouble(Double::doubleValue));
  }

  /**
   * Calcul le temps de preparation
   * @param productInCommandList
   * @return
   */
  public Integer calculateCommandPreparationTime(List<ProductInCommand> productInCommandList) {
    return productInCommandList
            .stream()
            .map(productInCommand -> {
              Integer productPreparationTime = productRepository
                      .findById(productInCommand.productId())
                      .orElseThrow()
                      .getPreparationTime();
              return productPreparationTime * productInCommand.productQuantity();
            })
            .collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Renvoie le nbre de produit dans une commande
   * @param productInCommandList
   * @return
   */
  public Integer calculatedNumberOfProductInCommand(List<ProductInCommand> productInCommandList) {
    return productInCommandList
            .stream()
            .map(ProductInCommand::productQuantity)
            .collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Création Liste procutInCommand pour une commande
   * @param storeId - store
   * @param commandId - commande
   * @return List<ProductInCommand>
   * @throws RuntimeException
   */
  public List<ProductInCommand> findAllProductInCommand(BigInteger storeId, BigInteger commandId) throws RuntimeException {

    return cookRepository.findByCommandIdAndStoreId(storeId, commandId).stream().map(cookItem-> {
      Integer productQuantity = cookItem.getProductQuantity();
      ProductEntity product = productRepository
              .findById(cookItem.getProduct().getId())
              .orElseThrow(()->new RuntimeException("Le produit n'existe pas"));
      return new ProductInCommand(product.getId(), productQuantity);
    }).collect(Collectors.toList());
  }

  /**
   * Retourne une liste de produit pour Id produit
   * @param productIdList
   * @return
   */
  public List<ProductEntity> findProductListById(List<BigInteger> productIdList) {
    return productIdList.stream()
            .map(id->{
              return productRepository.findById(id).orElseThrow(()->new RuntimeException("Le produit n'existe pas"));
            })
            .collect(Collectors.toList());
  }

  /**
   * Vérification si une liste de produit appartient a un store
   * @param productInCommandList
   * @param storeId
   * @return
   */
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
