package ctoutweb.lalamiam.model.dto;
public record ProductWithQuantityDto(
        Long productId,
        String name,
        String photo,
        Double price,
        Integer selectQuantity,
        Boolean isAvail
) {
}
