package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;

public record AddCookSchema(BigInteger productId, BigInteger commandId, BigInteger storeId, Integer productQuantity) {
}
