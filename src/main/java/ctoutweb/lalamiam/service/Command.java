package ctoutweb.lalamiam.service;

import ctoutweb.lalamiam.model.dto.CommandInformationDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface Command {
  CommandInformationDto getStoreProductsForCommand(BigInteger storeId, BigInteger commandId);
  void validateProductsSelection();
  List<LocalDateTime> displayStoreSlotAvailibility();
  void validateSlot();
  RegisterCommandDto persistCommand();

}
