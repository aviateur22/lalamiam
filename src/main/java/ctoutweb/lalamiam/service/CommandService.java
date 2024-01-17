package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.CommandDetailDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.AddProductsInCommandSchema;
import ctoutweb.lalamiam.model.schema.DeleteProductInCommandSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductQuantityInCommandSchema;

public interface CommandService {
  CommandDetailDto addCommand(AddCommandSchema addCommandSchema);
  UpdateProductQuantityInCommandDto updateProductQuantityInCommand(UpdateProductQuantityInCommandSchema updateCommandSchema);
  CommandDetailDto deleteProductInCommand(DeleteProductInCommandSchema deleteProductInCommand);
  CommandDetailDto addProductsInCommand(AddProductsInCommandSchema addProductsInCommand);
}
