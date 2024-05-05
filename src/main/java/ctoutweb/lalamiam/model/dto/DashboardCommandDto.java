package ctoutweb.lalamiam.model.dto;

import java.time.LocalDateTime;

public record DashboardCommandDto(
  Long commandId,
 String commandCode,
 int productQuantity,
 double commandePrice,
 int commandPreparationTime,
 int commandStatus,
 LocalDateTime slotTime
) { }
