package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;

public record DeleteProductInCommandDto(
        BigInteger commandId,
        BigInteger productId,
        BigInteger storeId
) {
}
