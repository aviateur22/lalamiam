package ctoutweb.lalamiam.model.dto;

import java.math.BigInteger;

public record AddStoreDto(
        BigInteger proId,
        String name,
        String adress,
        String city,
        String cp,
        Integer frequenceSlotTime) {
}
