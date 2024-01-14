package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.AddCommandDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductQuantityInCommandSchema;

public interface CommandService {
  AddCommandDto addCommand(AddCommandSchema addCommandSchema);
  UpdateProductQuantityInCommandDto updateProductQuantityInCommand(UpdateProductQuantityInCommandSchema updateCommandSchema);
}
