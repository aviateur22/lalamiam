package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;

public record UpdateProductDto(
        BigInteger productId,
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        BigInteger storeId) {
}
