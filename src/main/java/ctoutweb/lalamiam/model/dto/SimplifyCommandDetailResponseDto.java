package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.schema.ProductWithQuantity;

import java.math.BigInteger;
import java.util.List;

public record SimplifyCommandDetailDto(
        BigInteger commandId,
        List<ProductWithQuantity> productInCommandList,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice
) {}
