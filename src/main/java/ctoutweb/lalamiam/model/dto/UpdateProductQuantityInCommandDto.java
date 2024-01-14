package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.schema.ProductInCommand;

import java.math.BigInteger;
public record UpdateProductQuantityInCommandDto(
        BigInteger commandId,
        ProductInCommand productInCommand,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice) {
}
