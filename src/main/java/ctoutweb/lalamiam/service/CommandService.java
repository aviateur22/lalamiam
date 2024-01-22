package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CommandService {
  CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommandSchema);
  UpdateProductQuantityResponseDto updateProductQuantityInCommand(UpdateProductQuantityDto updateCommandSchema);
  SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand);
  SimplifyCommandDetailResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand);
  List<LocalDateTime> findAllSlotAvailable(FindSlotTimeDto findSlotTime);
}
