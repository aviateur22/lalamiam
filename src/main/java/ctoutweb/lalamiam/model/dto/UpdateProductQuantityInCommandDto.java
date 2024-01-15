package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.schema.ProductWithQuantity;

import java.math.BigInteger;
public record UpdateProductQuantityInCommandDto(
        BigInteger commandId,
        ProductWithQuantity productInCommand,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice) {
}
