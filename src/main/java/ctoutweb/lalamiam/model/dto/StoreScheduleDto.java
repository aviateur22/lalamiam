package ctoutweb.lalamiam.model.dto;

import java.time.LocalTime;

public record StoreScheduleDto(
   Integer dayId,
   LocalTime openingTime,
   LocalTime closingTime
) {}
