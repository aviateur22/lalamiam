package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
import java.util.List;

public record AddProductsInCommandDto(
        List<BigInteger> productIdList,
        BigInteger commandId) {
}
