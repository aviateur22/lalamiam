package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;

public interface CommandService {
  CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommandSchema);
  UpdateProductQuantityResponseDto updateProductQuantityInCommand(UpdateProductQuantityDto updateCommandSchema);
  SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand);
  SimplifyCommandDetailResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand);
}
