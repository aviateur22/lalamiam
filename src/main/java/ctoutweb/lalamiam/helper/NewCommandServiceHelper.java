package ctoutweb.lalamiam.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.service.CommandProductService;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class NewCommandServiceHelper {

  private final CommandProductService commandProductService;
  private final ProductQuantityMapper productQuantityMapper;

  public NewCommandServiceHelper(
          CommandProductService commandProductService,
          ProductQuantityMapper productQuantityMapper
  ) {
    this.commandProductService = commandProductService;
    this.productQuantityMapper = productQuantityMapper;
  }

  /**
   * Recherche des données d'une commande
   * @param storeId BigInteger - Identitifiant commerce
   * @param command CommandEntity - Identifiant commande
   * @return RegisterCommandDto
   */
  public RegisterCommandDto findRegisterCommandInformation(BigInteger storeId, CommandEntity command) {
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
  public List<ProductWithQuantityDto> getStoreProductsWithCommandQuantity(BigInteger storeId, RegisterCommandDto registerCommand) {
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

}
