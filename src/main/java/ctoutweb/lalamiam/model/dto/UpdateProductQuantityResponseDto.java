package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.schema.ProductWithQuantity;

import java.math.BigInteger;
public record UpdateProductInCommandDtoResponse(
        BigInteger commandId,
        ProductWithQuantity productInCommand,
        Integer commandPreparationTime,
        Integer ProductQuantity,
        Double commandPrice) {
}
