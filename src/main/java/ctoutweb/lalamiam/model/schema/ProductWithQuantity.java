package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;

public record ProductWithQuantity(
        BigInteger productId,
        Integer productQuantity
) {
}
