package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SelectCommandSlotDto(
        BigInteger StoreId,
        LocalDate commandDate,
        LocalDateTime selectedSlot
) {
}
