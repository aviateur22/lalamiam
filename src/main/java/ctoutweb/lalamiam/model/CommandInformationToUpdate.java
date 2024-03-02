package ctoutweb.lalamiam.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Information pour la mise a jour d'un commande
 * @param storeId
 * @param commandId
 * @param clientPhone
 * @param selectProducts
 * @param preparationTime
 * @param numberOfProductInCommand
 * @param commandPrice
 * @param slotTime
 */
public record CommandInformationToUpdate(
        Long storeId,
        Long commandId,
        String clientPhone,
        List<ProductWithQuantity> selectProducts,
        LocalDateTime slotTime,
        Integer preparationTime,
        Integer numberOfProductInCommand,
        Double commandPrice
) {
}
