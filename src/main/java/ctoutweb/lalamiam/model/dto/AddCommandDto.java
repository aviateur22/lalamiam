package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.schema.ProductInCommand;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record AddCommandDto(
        BigInteger commandId,
        List<ProductInCommand> productInCommandList,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice,
        String phoneClient,
        String commandCode,
        LocalDateTime commandSlotTime
) {

}
