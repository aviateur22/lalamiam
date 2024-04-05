package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.exception.ProductException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class NewCommandServiceHelper {
  private static final Logger LOGGER = LogManager.getLogger();
  private final ProductQuantityMapper productQuantityMapper;
  private final ProductService productService;

  public NewCommandServiceHelper(
          ProductQuantityMapper productQuantityMapper,
          ProductService productService) {
    this.productQuantityMapper = productQuantityMapper;
    this.productService = productService;
  }

  /**
   * Recherche des données d'une commande
   * @param command CommandEntity - Identifiant commande
   * @return RegisterCommandDto
   */
  public RegisterCommandDto findRegisterCommandInformation(CommandEntity command) {
    // Si commande en cours de création
    if(command == null) return null;

    // Récuperation des infos produits
    List<ProductEntity> productsInCommand = productService.getMultipleProductsOfStore(
            command.getCommandProducts()
        .stream()
        .mapToLong(commandPro->commandPro.getProduct().getId())
        .boxed()
        .collect(Collectors.toList()),
      command.getStore().getId());
    LOGGER.info(()->String.format("products: %s", productsInCommand));

    List<ProductWithQuantityDto> productsWithQuantityDto = command.getCommandProducts()
            .stream()
            .map(product->this.getProductWithQuantityDto(product, productsInCommand))
            .toList();

    return Factory.getRegisterCommand(command, productsWithQuantityDto);
  }

  /**
   * Récupération des information sur un produit
   * @param commandProduct CommandProductEntity - Produit à completer
   * @param productsList List<ProductEntity> - Liste des produits de la commande avec les informations complete
   * @return ProductWithQuantityDto
   */
  public ProductWithQuantityDto getProductWithQuantityDto(CommandProductEntity commandProduct, List<ProductEntity> productsList) {
    //Todo faire test
    ProductEntity findProduct = productsList
            .stream()
            .filter(product->product.getId().equals(commandProduct.getProduct().getId()))
            .findFirst()
            .orElse(null);

    if(findProduct == null) throw new ProductException("Produit absent", HttpStatus.BAD_REQUEST);

    return new ProductWithQuantityDto(findProduct.getId(), findProduct.getName(), findProduct.getPhoto(), findProduct.getPrice(), commandProduct.getProductQuantity(), findProduct.getIsAvail());

  }

  /**
   * Renvoie les produits d'un commerce mapper avec les quantité d'une commande
   * (quantité est = 0 si création d'une commande ou produit absent de la commande)
   * @param storeId Long - Identitifiant commerce
   * @param registerCommand RegisterCommandDto - données sur la commande
   * @return List<ProductWithQuantityDto>
   */
  public List<ProductWithQuantityDto> getStoreProductsWithCommandQuantity(
          Long storeId,
          RegisterCommandDto registerCommand
  ) {
    final int CREATE_COMMAND_QUANTITY = 0;

    // Récupération des produits du commerce
    List<ProductEntity> storeProducts = productService.getStoreProducts(storeId);

    if(storeProducts.size() == 0) return Arrays.asList();

    // Si commande en cours de création
    if(registerCommand == null || registerCommand.getCommandId() == null)
      return storeProductWithQuantity(storeProducts, CREATE_COMMAND_QUANTITY);

    return storeProductWithQuantity(storeProducts, registerCommand, CREATE_COMMAND_QUANTITY);
  }



  /**
   * Map les produits d'un commerce avec une quantité = 0
   * @param storeProducts List<ProductEntity> - Produits du commerce
   * @param CREATE_COMMAND_QUANTITY Integer - Quantité par default si produit non présent dans la cammande
   * @return List<ProductWithQuantityDto>
   */
  public List<ProductWithQuantityDto> storeProductWithQuantity(List<ProductEntity> storeProducts, final int CREATE_COMMAND_QUANTITY ) {
    // Todo test unitaire
    return storeProducts
            .stream()
            .map(storeProduct -> {
              return Factory.getProductWithQuantityDto(storeProduct, CREATE_COMMAND_QUANTITY);
            })
            .toList();
  }

  /**
   * Map les produits d'un commerce avec une quantité des produits d'une commande
   * @param storeProducts List<ProductEntity> - Produits du commerce
   * @param registerCommand RegisterCommandDto - Données sur la commande existante
   * @param CREATE_COMMAND_QUANTITY Integer - Quantité par default si produit non présent dans la cammande
   * @return List<ProductWithQuantityDto>
   */
  public List<ProductWithQuantityDto> storeProductWithQuantity(
          List<ProductEntity> storeProducts,
          RegisterCommandDto registerCommand,
          final int CREATE_COMMAND_QUANTITY
  ) {
    // Todo test unitaire
    return storeProducts
            .stream()
            .map(storeProduct->{
              return registerCommand.getManualCommandInformation().getSelectProducts()
                      .stream()
                      .filter(productCommand->productCommand.productId().equals(storeProduct.getId()))
                      .findFirst()
                      .map(productCommand-> Factory.getProductWithQuantityDto(storeProduct, productCommand.selectQuantity()))
                      .orElse(Factory.getProductWithQuantityDto(storeProduct, CREATE_COMMAND_QUANTITY));
            })
            .toList();
  }



  /**
   * Calcul du temps de préparation d'une commande
   * @param productsSelected List<ProductWithQuantity> - Liste des produits selectionnés pour la commande
   * @return Integer
   */
  public Integer calculateCommandPreparationTime(List<ProductWithQuantity> productsSelected) {
    if(productsSelected == null || productsSelected.isEmpty()) return null;
    return productsSelected
      .stream()
      .map(productWithQuantity -> {
        ProductEntity product = productService.findProduct(productWithQuantity.getProductId());
        return product.getPreparationTime() * productWithQuantity.getProductQuantity();
      }).collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Calcul le prix d'une commande
   * @param selectProducts List<ProductWithQuantity> - Liste des produits selectionnés pour la commande
   * @return Double - Prix de la commande
   */
  public Double calculateCommandPrice(List<ProductWithQuantity> selectProducts) {

    if(selectProducts == null || selectProducts.isEmpty()) return null;
    return selectProducts
            .stream()
            .map(productWithQuantity -> {
              ProductEntity product = productService.findProduct(productWithQuantity.getProductId());
              return product.getPrice() * productWithQuantity.getProductQuantity();
            }).collect(Collectors.summingDouble(Double::doubleValue));
  }

  /**
   * Calcul le nombre de produits dans une commande
   * @param selectProducts List<ProductWithQuantity> - Liste des produits selectionnés pour la commande
   * @return Integer - Produit dans une commande
   */
  public Integer calculateNumberOfProductInCommand(List<ProductWithQuantity> selectProducts) {
    if(selectProducts == null || selectProducts.isEmpty()) return null;
    return selectProducts
            .stream()
            .map(productWithQuantity -> productWithQuantity.getProductQuantity())
            .collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Génération d'un code aléatoire
   * @param wordLength Integer - Longueur du code
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
}
