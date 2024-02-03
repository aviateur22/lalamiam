package ctoutweb.lalamiam.model;

import java.util.List;

/**
 * Horaires commerce associés à une liste de jour
 * @param days
 * @param storeSchedule
 */
public record WeeklyStoreSchedule(List<Integer> days, DailyStoreSchedule storeSchedule) {}
