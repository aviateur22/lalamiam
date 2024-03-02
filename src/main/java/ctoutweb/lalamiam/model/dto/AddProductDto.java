package ctoutweb.lalamiam.model.dto;
public record AddProductDto(
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        Long storeId) {
}
