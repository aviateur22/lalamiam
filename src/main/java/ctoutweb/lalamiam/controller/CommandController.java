package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.AddCommandDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductQuantityInCommandSchema;
import ctoutweb.lalamiam.service.CommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lalamiam/v1/command")
public class CommandController {

  private final CommandService commandService;

  public CommandController(CommandService commandService) {
    this.commandService = commandService;
  }

  @PostMapping("/")
  ResponseEntity<AddCommandDto> AddCommand(@RequestBody AddCommandSchema addCommandSchema) {
    AddCommandDto addCommand = commandService.addCommand(addCommandSchema);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }
  @PatchMapping("/update-product-quantity-in-command")
  UpdateProductQuantityInCommandDto updateProductQuantity(@RequestBody UpdateProductQuantityInCommandSchema updateProductQuantity) {
    return commandService.updateProductQuantityInCommand(updateProductQuantity);
  }
}
