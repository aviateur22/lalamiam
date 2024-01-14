package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record AddProductDto(
        BigInteger id,
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
