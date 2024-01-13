package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;

public record UpdateProductCommandSchema(BigInteger productId, BigInteger commandId, BigInteger storeId, Integer productQuantity) {
}
