package ctoutweb.lalamiam.model.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Données a afficher pour la creation ou modification d'une commande
 * @param storeProducts List<ProductWithQuantityDto> - liste des produits du commerce comportant les quantités déja commandé
 * @param clientPhone String
 * @param slotTime Date de la commande
 */
public record CommandInformationDto(
   List<ProductWithQuantityDto> storeProducts,
   String clientPhone,
   LocalDateTime slotTime
) {}
