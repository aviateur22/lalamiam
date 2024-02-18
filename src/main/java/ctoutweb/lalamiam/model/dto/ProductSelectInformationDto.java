package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.time.LocalDate;
import java.util.List;

/**
 * Produits selectionnés dans la commande
 * @param productSelectList List<ProductWithQuantity> - Id du produit avec la quantité associés
 * @param clientPhone String - N°Tel
 */
public record ProductSelectInformationDto(
        List<ProductWithQuantity> productSelectList,
        String clientPhone
) {}
