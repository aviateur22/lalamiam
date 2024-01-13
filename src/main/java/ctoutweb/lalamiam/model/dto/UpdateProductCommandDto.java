package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;
public record UpdateProductCommandDto(
        BigInteger productId,
        Integer productQuantity,
        Integer commandPreparationTime,
        Double commandPrice) {
}
