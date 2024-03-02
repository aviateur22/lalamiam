package ctoutweb.lalamiam.model.dto;

public record UpdateProductDto(
        Long productId,
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        Long storeId) {
}
