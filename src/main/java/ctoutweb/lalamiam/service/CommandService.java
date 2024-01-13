package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.ProductInCommand;
import ctoutweb.lalamiam.model.schema.UpdateProductCommandSchema;
import ctoutweb.lalamiam.repository.entity.CommandEntity;

public interface CommandService {
  CommandEntity addCommand(AddCommandSchema addCommandSchema);

  ProductInCommand updateProductCommand(UpdateProductCommandSchema updateCommandSchema);
}
