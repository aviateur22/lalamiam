package ctoutweb.lalamiam.model.dto;

import java.time.LocalDateTime;

public record AddProductResponseDto(
        Long id,
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
