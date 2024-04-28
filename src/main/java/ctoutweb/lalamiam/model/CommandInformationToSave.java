package ctoutweb.lalamiam.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Données pour sauvegarder une commande
 * @param storeId
 * @param commandCode
 * @param clientPhone
 * @param selectProducts
 * @param preparationTime
 * @param numberOfProductInCommand
 * @param commandPrice
 * @param slotTime
 */
public record CommandInformationToSave(
        Long storeId,
        String clientPhone,
        Long userId,
        List<ProductWithQuantity> selectProducts,
        LocalDateTime slotTime,
        String commandCode,
        Integer preparationTime,
        Integer numberOfProductInCommand,
        Double commandPrice
) {}
