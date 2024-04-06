package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/***
 * Persistance d'une commande par un client
 * @param storeId
 * @param commandId
 * @param commandDate
 * @param consultationDate
 * @param selectProducts
 * @param clientPhone
 * @param selectSlotTime
 */
public record ClientPersitCommandDto(
        Long storeId,
        Long clientId,
        Long commandId,
        LocalDate commandDate,
        LocalDateTime consultationDate,
        List<ProductWithQuantity> selectProducts,
        String clientPhone,
        LocalDateTime selectSlotTime
) {
}
