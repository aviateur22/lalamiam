package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.service.CommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("api/lalamiam/v1/command")
public class CommandController {
  private final CommandService commandService;

  public CommandController(CommandService commandService) {
    this.commandService = commandService;
  }

  @PostMapping("/")
  ResponseEntity<CompleteCommandDetailResponseDto> AddCommand(@RequestBody AddCommandDto addCommandSchema) {
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema);
    return new ResponseEntity<>(addCommand, HttpStatus.OK);
  }
  @PatchMapping("/update-product-quantity-in-command")
  UpdateProductQuantityResponseDto updateProductQuantity(@RequestBody UpdateProductQuantityDto updateProductQuantity) {
    return commandService.updateProductQuantityInCommand(updateProductQuantity);
  }

}
