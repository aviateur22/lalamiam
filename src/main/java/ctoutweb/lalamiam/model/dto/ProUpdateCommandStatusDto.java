package ctoutweb.lalamiam.model.dto;

public record ProUpdateCommandStatusDto(
        Long storeId,
        Long commandId,
        Long proId,
        Integer commandStatus
) {
}
