package ctoutweb.lalamiam.model.dto;

import java.time.LocalDateTime;

public record DashboardCommandDto(
 Long commandId,
 String commandCode,
 int productQuantity,
 double commandPrice,
 int commandPreparationTime,
 int commandStatus,
 LocalDateTime slotTime
) { }
