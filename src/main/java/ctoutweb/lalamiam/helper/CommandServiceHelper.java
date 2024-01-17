package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.model.dto.CommandDetailDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.*;
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
  public CommandDetailDto addCommand(AddCommandSchema addCommandSchema) throws RuntimeException {

    // Vérification si les produits appartiennent au Store
    if(!isProductListBelongToStore(addCommandSchema.productsInCommand(), addCommandSchema.storeId()))
      throw new RuntimeException("Le produit n'est pas rattaché au store");

    // Prix de la commande
    Double commandPrice = calculateCommandPrice(addCommandSchema.productsInCommand());

    // Temps de preparation
    Integer commandPrepartionTime = calculateCommandPreparationTime(addCommandSchema.productsInCommand());

    Integer productQuantity = calculatedNumberOfProductInCommand(addCommandSchema.productsInCommand());

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
                    .withProductQuantity(productQuantity)
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
    List<ProductWithQuantity> productInCommandList = findAllProductInCommand(addCommandSchema.storeId(), addCommand.getId());

    // Calcul quantité produit
    Integer numberOfProductInCommande = calculatedNumberOfProductInCommand(productInCommandList);


    return new CommandDetailDto(
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
    List<ProductWithQuantity> productInCommandList = findAllProductInCommand(
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
    updatedCommand.setProductQuantity(totalProductInCommand);
    commandRepository.save(updatedCommand);

    return new UpdateProductQuantityInCommandDto(
            updatedCommand.getId(),
            new ProductWithQuantity(productUpdate.getProduct().getId(), productUpdate.getProductQuantity()),
            commandPrepartionTime,
            totalProductInCommand,
            commandPrice);

  }

  /**
   * Suppression produit dans 1 commande
   * @param deleteProductInCommand - Produit a supprimmé
   * @return CommandDetailDto
   */
  public CommandDetailDto deleteProductInCommand(DeleteProductInCommandSchema deleteProductInCommand) {

    // Récuperation des données de la commande
    CommandEntity command = commandRepository
            .findById(deleteProductInCommand.commandId())
            .orElseThrow(()->new RuntimeException("La commande n'est pas trouvée"));

    // Suppression du produit
    cookRepository.deleteProductInCommandByCommandIdStoreIdProductId(
            deleteProductInCommand.commandId(),
            deleteProductInCommand.productId(),
            deleteProductInCommand.storeId()
    );

    // Recherche de la liste des produits en commande
    List<ProductWithQuantity> productInCommandList = findAllProductInCommand(
            deleteProductInCommand.storeId(),
            deleteProductInCommand.commandId());

    // Calcul du nouveau prix de la commande
    Double commandPrice = calculateCommandPrice(productInCommandList);

    // Calcul du nouveau du temps de preparation de la commande
    Integer commandPrepartionTime = calculateCommandPreparationTime(productInCommandList);

    // Calcul quantité produit
    Integer numberOfProductInCommande = calculatedNumberOfProductInCommand(productInCommandList);

    // Mise a jour en base de données des données de la commande
    command.setPreparationTime(commandPrepartionTime);
    command.setOrderPrice(commandPrice);
    command.setProductQuantity(numberOfProductInCommande);
    commandRepository.save(command);

    return new CommandDetailDto(
            deleteProductInCommand.commandId(),
            productInCommandList,
            commandPrepartionTime,
            numberOfProductInCommande,
            commandPrice,
            command.getClientPhone(),
            command.getCommandCode(),
            command.getSlotTime());
  }

  /**
   * Ajout d'un ou plusieurs produits dans la commande
   * @param addProductsInCommand
   * @return CommandDetailDto
   */
  public CommandDetailDto addProductsInCommand(AddProductsInCommandSchema addProductsInCommand) {
    addProductsInCommand.productIdList().stream().forEach(productId->{
      CookEntity addProduct = CookEntityBuilder.aCookEntity()
              .withCommand(new CommandEntity(addProductsInCommand.commandId()))
              .withProductQuantity(1)
              .withStore(new StoreEntity(addProductsInCommand.storeId()))
              .withProduct(new ProductEntity(productId))
              .build();
      addProduct = cookRepository.save(addProduct);
    });

    // Recherche de la liste des produits en commande
    List<ProductWithQuantity> productInCommandList = findAllProductInCommand(
            addProductsInCommand.storeId(),
            addProductsInCommand.commandId());

    // Calcul du nouveau prix de la commande
    Double commandPrice = calculateCommandPrice(productInCommandList);

    // Calcul du nouveau du temps de preparation de la commande
    Integer commandPrepartionTime = calculateCommandPreparationTime(productInCommandList);

    // Calcul quantité produit
    Integer numberOfProductInCommande = calculatedNumberOfProductInCommand(productInCommandList);

    // Mise a jour en base de données des données de la commande
    CommandEntity command = commandRepository.findById(addProductsInCommand.commandId()).orElseThrow();
    command.setPreparationTime(commandPrepartionTime);
    command.setOrderPrice(commandPrice);
    command.setProductQuantity(numberOfProductInCommande);
    commandRepository.save(command);

    return new CommandDetailDto(
            command.getId(),
            productInCommandList,
            commandPrepartionTime,
            numberOfProductInCommande,
            commandPrice,
            command.getClientPhone(),
            command.getCommandCode(),
            command.getSlotTime());
  }
  /**
   * Calcul le prix d'une commande
   * @param productInCommandList
   * @return
   */
  public Double calculateCommandPrice(List<ProductWithQuantity> productInCommandList) throws RuntimeException {
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
  public Integer calculateCommandPreparationTime(List<ProductWithQuantity> productInCommandList) {
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
  public Integer calculatedNumberOfProductInCommand(List<ProductWithQuantity> productInCommandList) {
    return productInCommandList
            .stream()
            .map(ProductWithQuantity::productQuantity)
            .collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Création Liste procutInCommand pour une commande
   * @param storeId - store
   * @param commandId - commande
   * @return List<ProductInCommand>
   * @throws RuntimeException
   */
  public List<ProductWithQuantity> findAllProductInCommand(BigInteger storeId, BigInteger commandId) throws RuntimeException {

    return cookRepository.findByCommandIdAndStoreId(storeId, commandId).stream().map(cookItem-> {
      Integer productQuantity = cookItem.getProductQuantity();
      ProductEntity product = productRepository
              .findById(cookItem.getProduct().getId())
              .orElseThrow(()->new RuntimeException("Le produit n'existe pas"));
      return new ProductWithQuantity(product.getId(), productQuantity);
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
  public boolean isProductListBelongToStore(List<ProductWithQuantity> productInCommandList, BigInteger storeId) {
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
