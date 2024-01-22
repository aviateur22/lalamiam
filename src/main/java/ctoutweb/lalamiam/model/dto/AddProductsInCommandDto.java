package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
import java.util.List;

public record AddProductsInCommandDto(

        BigInteger storeId,
        List<BigInteger> productIdList,
        BigInteger commandId) {
}
