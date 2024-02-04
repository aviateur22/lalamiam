package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.math.BigInteger;
public record UpdateProductQuantityResponseDto(
        BigInteger commandId,
        ProductWithQuantity productInCommand,
        Integer commandPreparationTime,
        Integer ProductQuantity,
        Double commandPrice) {}
