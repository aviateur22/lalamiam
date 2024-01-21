package ctoutweb.lalamiam.model;

import java.util.List;

/**
 * Données de la commande calculé a partir de la liste des produits
 * @param productInCommandList
 * @param commandPreparationTime
 * @param numberOProductInCommand
 * @param commandPrice
 */
public record CalculateCommandDetail(
        List<ProductWithQuantity> productInCommandList,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice
) {}
