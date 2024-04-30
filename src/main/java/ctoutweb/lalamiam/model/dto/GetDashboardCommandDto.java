package ctoutweb.lalamiam.model.dto;

import java.time.LocalDate;

public record GetDashboardCommandDto(
        LocalDate commandDate,
        Long proId,
        Long storeId
) {
}
