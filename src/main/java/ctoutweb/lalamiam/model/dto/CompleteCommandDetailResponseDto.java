package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record CompleteCommandDetailResponseDto(
        BigInteger commandId,
        List<ProductWithQuantity> productInCommandList,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice,
        String phoneClient,
        String commandCode,
        LocalDateTime commandSlotTime
) {}