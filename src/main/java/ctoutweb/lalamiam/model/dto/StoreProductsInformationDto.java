package ctoutweb.lalamiam.model.dto;

import java.util.List;

/**
 * Données a afficher pour la creation ou modification d'une commande
 * @param storeProducts List<ProductWithQuantityDto> - liste des produits du commerce comportant les quantités déja commandé
 * @param clientPhone String
 */
public record StoreProductsInformationDto(
   List<ProductWithQuantityDto> storeProducts,
   String clientPhone
) {}
