package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.CommandProductService;
import ctoutweb.lalamiam.service.ProductService;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class NewCommandServiceHelper {

  private final CommandProductService commandProductService;
  private final ProductQuantityMapper productQuantityMapper;
  private final ProductService productService;

  public NewCommandServiceHelper(
          CommandProductService commandProductService,
          ProductQuantityMapper productQuantityMapper,
          ProductService productService) {
    this.commandProductService = commandProductService;
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

    List<ProductWithQuantityDto> productsWithQuantityDto = command.getCommandProducts()
            .stream()
            .map(productQuantityMapper)
            .toList();

    return Factory.getRegisterCommand(command, productsWithQuantityDto);
  }

  /**
   * Renvoie les produits d'un commerce mapper avec les quantité d'une commande
   * (quantité est = 0 si création d'une commande ou produit absent de la commande)
   * @param storeId BigInteger - Identitifiant commerce
   * @param registerCommand RegisterCommandDto - données sur la commande
   * @return List<ProductWithQuantityDto>
   */
  public List<ProductWithQuantityDto> getStoreProductsWithCommandQuantity(
          BigInteger storeId,
          RegisterCommandDto registerCommand
  ) {
    final int CREATE_COMMAND_QUANTITY = 0;

    // Si commande en cours de création
    if(registerCommand == null) return commandProductService.getStoreProducts(storeId)
            .stream()
            .map(storeProduct -> {
              return Factory.getProductWithQuantityDto(storeProduct, CREATE_COMMAND_QUANTITY);
            })
            .toList();


    return commandProductService.getStoreProducts(storeId)
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
