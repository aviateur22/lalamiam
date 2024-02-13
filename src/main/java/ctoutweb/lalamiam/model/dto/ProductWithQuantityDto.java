package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;

public record ProductWithQuantityDto(
        BigInteger productId,
        String name,
        String photo,
        Double price,
        Integer selectQuantity,
        Boolean isAvail
) {
}
