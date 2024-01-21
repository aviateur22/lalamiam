package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;
import java.util.List;

public record AddProductsInCommandSchema(
        List<BigInteger> productIdList,
        BigInteger commandId) {
}
