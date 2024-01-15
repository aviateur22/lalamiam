package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;

public record DeleteProductInCommandSchema(
        BigInteger commandId,
        BigInteger productId,
        BigInteger storeId
) {
}
