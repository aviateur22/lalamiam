package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommandService {
  CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommandSchema);
  UpdateProductQuantityResponseDto updateProductQuantityInCommand(UpdateProductQuantityDto updateCommandSchema);
  SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand);
  AddProductsInCommandResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand);
  List<LocalDateTime> findAllSlotAvailable(FindListOfSlotTimeAvailableDto findSlotTime);
}
