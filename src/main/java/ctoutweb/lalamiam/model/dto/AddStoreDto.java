package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.StoreSchedule;

import java.math.BigInteger;
import java.util.List;

public record AddStoreDto(
        BigInteger proId,
        String name,
        String adress,
        String city,
        String cp,
        List<StoreSchedule> storeSchedule,
        Integer frequenceSlotTime) {
}
