package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.math.BigInteger;
import java.util.List;

/**
 * Liste des produits ajouté a une commande
 * @param commandId
 * @param addProducts
 * @param commandPreparationTime
 * @param numberOProductInCommand
 * @param commandPrice
 */
public record AddProductsInCommandResponseDto(
        BigInteger commandId,
        List<ProductWithQuantity> addProducts,
        Integer commandPreparationTime,
        Integer numberOProductInCommand,
        Double commandPrice
) {}
