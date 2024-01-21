package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AddProductMapper implements Function<ProductEntity, AddProductResponseDto> {
  @Override
  public AddProductResponseDto apply(ProductEntity productEntity) {
    return new AddProductResponseDto(
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
