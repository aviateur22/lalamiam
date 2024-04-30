package ctoutweb.lalamiam.model.dto;

import java.time.LocalDateTime;

public record DashboardCommandDto(
 String commandCode,
 int productQuantity,
 double commandePrice,
 int commandPreparationTime,
 LocalDateTime slotTime
) { }
