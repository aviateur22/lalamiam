package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.math.BigInteger;
import java.util.List;

public record AddProductsInCommandDto(
        BigInteger storeId,
        List<ProductWithQuantity> productWithQuantityList,
        BigInteger commandId) {
}
