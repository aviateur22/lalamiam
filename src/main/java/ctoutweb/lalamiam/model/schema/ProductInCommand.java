package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;

public record ProductInCommand(
        BigInteger productId,
        Integer productQuantity
) {
}
