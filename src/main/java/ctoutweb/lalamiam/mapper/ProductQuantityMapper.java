package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class ProductQuantityMapper implements Function<CommandProductEntity, ProductWithQuantityDto> {

  @Override
  public ProductWithQuantityDto apply(CommandProductEntity commandProduct) {
    return Factory.getProductWithQuantityDto(commandProduct.getProduct(), commandProduct.getProductQuantity());
  }
}
