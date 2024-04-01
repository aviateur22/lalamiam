package ctoutweb.lalamiam.model.dto;
public record AddProductDto(
        Long proId,
        String name,
        Double price,
        String description,
        Integer preparationTime,
        String photo,
        Long storeId) {
}
