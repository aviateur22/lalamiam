package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.WeeklyStoreSchedule;

import java.util.List;

public record AddStoreDto(
        Long proId,
        String name,
        String adress,
        String city,
        String cp,
        List<WeeklyStoreSchedule> weeklyStoreSchedules,
        Integer frequenceSlotTime) {
}
