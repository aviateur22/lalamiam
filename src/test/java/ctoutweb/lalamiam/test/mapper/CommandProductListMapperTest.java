package ctoutweb.lalamiam.test.mapper;

import ctoutweb.lalamiam.mapper.CommandProductListMapper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CommandProductListMapperTest {

  @Test
  void mapp_productWithQuantity_list_to_commandProduct_list() {
    CommandProductListMapper commandProductListMapper = new CommandProductListMapper();

    List<ProductWithQuantity> selectProducts = Arrays.asList(
            new ProductWithQuantity(Long.valueOf(1),2),
            new ProductWithQuantity(Long.valueOf(2),2),
            new ProductWithQuantity(Long.valueOf(3),1),
            new ProductWithQuantity(Long.valueOf(4),1)
    );

    List<CommandProductEntity> commandProductEntityList = commandProductListMapper.apply(selectProducts, Long.valueOf(1));

    Assertions.assertEquals(4, commandProductEntityList.size());

    Assertions.assertEquals(
            new CommandProductEntity(2, new ProductEntity(Long.valueOf(1)))
                    .getProduct().getId(), commandProductEntityList.get(0).getProduct().getId());
    Assertions.assertEquals(
            new CommandProductEntity(2, new ProductEntity(Long.valueOf(1)))
                    .getProductQuantity(), commandProductEntityList.get(0).getProductQuantity());

    Assertions.assertEquals(
            new CommandProductEntity(2, new ProductEntity(Long.valueOf(2)))
                    .getProduct().getId(), commandProductEntityList.get(1).getProduct().getId());
    Assertions.assertEquals(
            new CommandProductEntity(2, new ProductEntity(Long.valueOf(2)))
                    .getProduct().getId(), commandProductEntityList.get(1).getProduct().getId());

    Assertions.assertEquals(
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(3)))
                    .getProduct().getId(), commandProductEntityList.get(2).getProduct().getId());
    Assertions.assertEquals(
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(3)))
                    .getProduct().getId(), commandProductEntityList.get(2).getProduct().getId());

    Assertions.assertEquals(
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(4)))
                    .getProduct().getId(), commandProductEntityList.get(3).getProduct().getId());
    Assertions.assertEquals(
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(4)))
                    .getProduct().getId(), commandProductEntityList.get(3).getProduct().getId());


  }
}
