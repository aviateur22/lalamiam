package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.CommandProductService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CommandProductServiceImpl implements CommandProductService {


//  @Override
//  public AddProductsInCommandResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand) {
//    commandProductServiceHelper.isStoreValid(addProductsInCommand.storeId());
//    commandProductServiceHelper.isCommandValid(addProductsInCommand.storeId(), addProductsInCommand.commandId());
//    if(!commandProductServiceHelper.areProductsValid(
//            addProductsInCommand.productWithQuantityList()
//            .stream()
//            .map(ProductWithQuantity::getProductId)
//            .toList(),
//            addProductsInCommand.storeId())
//    )
//      throw new RuntimeException ("Certains produits à ajouter ne sont pas rattachés au commerce");;
//
//    return commandProductServiceHelper.addProductsInCommand(addProductsInCommand);
//  }
//
//  @Override
//  public List<ProductEntity> getStoreProducts(BigInteger storeId, BigInteger commandId) {
//    return null;
//  }

  @Override
  public List<ProductEntity> getStoreProducts(BigInteger storeId) {
    return null;
  }
}
