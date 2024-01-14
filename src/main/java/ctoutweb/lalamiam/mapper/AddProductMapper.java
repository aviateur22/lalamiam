package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AddProductMapper implements Function<ProductEntity, AddProductDto> {
  @Override
  public AddProductDto apply(ProductEntity productEntity) {
    return new AddProductDto(
            productEntity.getId(),
            productEntity.getName(),
            productEntity.getPrice(),
            productEntity.getDescription(),
            productEntity.getPreparationTime(),
            productEntity.getPhoto(),
            productEntity.getCreatedAt(),
            productEntity.getUpdatedAt());
  }
}
