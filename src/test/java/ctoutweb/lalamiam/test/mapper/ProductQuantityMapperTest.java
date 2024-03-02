package ctoutweb.lalamiam.test.mapper;

import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;


public class ProductQuantityMapperTest {
  @Test
  void ProductQuantityMapper_return_ProductQuantityMapperDto() {
    /**
     * Given
     */
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    /**
     * When
     */
    ProductWithQuantityDto productWithQuantityDto = productQuantityMapper.apply(fakeCommandProduct());

    /**
     * Then
     */
    Assertions.assertEquals(fakeProductWithQuantity().getProductId(), productWithQuantityDto.productId());
    Assertions.assertEquals(fakeProductWithQuantity().getProductQuantity(), productWithQuantityDto.selectQuantity());
    Assertions.assertEquals(fakeCommandProduct().getProduct().getIsAvail(), productWithQuantityDto.isAvail());
    Assertions.assertEquals(fakeCommandProduct().getProduct().getName(), productWithQuantityDto.name());
    Assertions.assertEquals(fakeCommandProduct().getProduct().getPhoto(), productWithQuantityDto.photo());
  }

  private ProductWithQuantity fakeProductWithQuantity() {
    return new ProductWithQuantity(Long.valueOf(1), 2);
  }

  private CommandProductEntity fakeCommandProduct() {
    return new CommandProductEntity(
        2, new ProductEntity(Long.valueOf(1),"test", 10D , "description", 5,"photo", true)
    );
  }
}
