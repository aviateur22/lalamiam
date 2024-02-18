package ctoutweb.lalamiam.test.helper;

import ctoutweb.lalamiam.helper.NewSlotHelper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.StoreDayScheduleEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.repository.entity.WeekDayEntity;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewSlotHelperTest {

  @Test
  void getStoreSlotAvailibility_without_command() {
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(12,00,00);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Mock StoreEntity StoreEntity
    when(storeService.findStoreById(any(BigInteger.class))).thenReturn(fakeStore);

    // Mock List<LocalDateTime> SlotsWithoutConstraintByDay
    when(storeService.findStorSlotsWithoutConstraintByDay(
            any(LocalDateTime.class),
            eq(fakeStore.getFrequenceSlotTime()))
    ).thenReturn(getFakeStoreSlotsWithoutConstraintByDay(fakeStore.getFrequenceSlotTime(),mondayDate.atStartOfDay()));

    // Mock List<StoreDayScheduleEntity> storeSchedules
    when(storeService.findStoreSchedulesByDay(eq(fakeStore), any(WeekDayEntity.class))).thenReturn(getFakeStoreSchedule(weekDay, fakeStore));

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    // Recherche créneaux dispo sans commande
   List<LocalDateTime> slotsAvail = slotHelper.getStoreSlotAvailibility(storeId, Arrays.asList());

   Assertions.assertEquals(28, slotsAvail.size());
  }

  @Test
  void getStoreSlotAvailibility_with_command() {
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(12,00,00);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Mock StoreEntity StoreEntity
    when(storeService.findStoreById(any(BigInteger.class))).thenReturn(fakeStore);

    // Mock List<LocalDateTime> SlotsWithoutConstraintByDay
    when(storeService.findStorSlotsWithoutConstraintByDay(
            any(LocalDateTime.class),
            eq(fakeStore.getFrequenceSlotTime()))
    ).thenReturn(getFakeStoreSlotsWithoutConstraintByDay(fakeStore.getFrequenceSlotTime(),mondayDate.atStartOfDay()));

    // Mock List<StoreDayScheduleEntity> storeSchedules
    when(storeService.findStoreSchedulesByDay(eq(fakeStore), any(WeekDayEntity.class))).thenReturn(getFakeStoreSchedule(weekDay, fakeStore));

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    // Recherche créneaux dispo avc des commandes en cours
    List<LocalDateTime> slotsAvail = slotHelper.getStoreSlotAvailibility(storeId, getFakeCommands(mondayDate));

    Assertions.assertEquals(26, slotsAvail.size());

    // Vérification que les crenaux occupé ne soit pas présent dans la liste des créneaux disponible
    Assertions.assertEquals(0, slotsAvail
            .stream()
            .filter(slot->getFakeCommands(mondayDate).contains(slot))
            .toList()
            .size());
  }

  @Test
  void getStoreSlotAvailibility_without_command_two_schedule_by_day(){
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(12,00,00);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Mock StoreEntity StoreEntity
    when(storeService.findStoreById(any(BigInteger.class))).thenReturn(fakeStore);

    // Mock List<LocalDateTime> SlotsWithoutConstraintByDay
    when(storeService.findStorSlotsWithoutConstraintByDay(
            any(LocalDateTime.class),
            eq(fakeStore.getFrequenceSlotTime()))
    ).thenReturn(getFakeStoreSlotsWithoutConstraintByDay(fakeStore.getFrequenceSlotTime(),mondayDate.atStartOfDay()));

    // Mock List<StoreDayScheduleEntity> storeSchedules
    when(storeService.findStoreSchedulesByDay(eq(fakeStore), any(WeekDayEntity.class))).thenReturn(getFakeDoubleStoreSchedule(weekDay, fakeStore));

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    // Recherche créneaux dispo sans commande
    List<LocalDateTime> slotsAvail = slotHelper.getStoreSlotAvailibility(storeId, Arrays.asList());

    Assertions.assertEquals(35, slotsAvail.size());
  }

  @Test
  void getStoreSlotAvailibility_with_command_two_schedule_by_day(){
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(12,00,00);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Mock StoreEntity StoreEntity
    when(storeService.findStoreById(any(BigInteger.class))).thenReturn(fakeStore);

    // Mock List<LocalDateTime> SlotsWithoutConstraintByDay
    when(storeService.findStorSlotsWithoutConstraintByDay(
            any(LocalDateTime.class),
            eq(fakeStore.getFrequenceSlotTime()))
    ).thenReturn(getFakeStoreSlotsWithoutConstraintByDay(fakeStore.getFrequenceSlotTime(),mondayDate.atStartOfDay()));

    // Mock List<StoreDayScheduleEntity> storeSchedules
    when(storeService.findStoreSchedulesByDay(eq(fakeStore), any(WeekDayEntity.class))).thenReturn(getFakeDoubleStoreSchedule(weekDay, fakeStore));

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    // Recherche créneaux dispo sans commande
    List<LocalDateTime> slotsAvail = slotHelper.getStoreSlotAvailibility(storeId, getFakeCommands(mondayDate));

    Assertions.assertEquals(33, slotsAvail.size());

    // Vérification que les crenaux occupé ne soit pas présent dans la liste des créneaux disponible
    Assertions.assertEquals(0, slotsAvail
            .stream()
            .filter(slot->getFakeCommands(mondayDate).contains(slot))
            .toList()
            .size());
  }

  @Test
  void getStoreSlotAvailibility_with_command_before_open(){
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(8,00,00);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Mock StoreEntity StoreEntity
    when(storeService.findStoreById(any(BigInteger.class))).thenReturn(fakeStore);

    // Mock List<LocalDateTime> SlotsWithoutConstraintByDay
    when(storeService.findStorSlotsWithoutConstraintByDay(
            any(LocalDateTime.class),
            eq(fakeStore.getFrequenceSlotTime()))
    ).thenReturn(getFakeStoreSlotsWithoutConstraintByDay(fakeStore.getFrequenceSlotTime(),mondayDate.atStartOfDay()));

    // Mock List<StoreDayScheduleEntity> storeSchedules
    when(storeService.findStoreSchedulesByDay(eq(fakeStore), any(WeekDayEntity.class))).thenReturn(getFakeStoreSchedule(weekDay, fakeStore));

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    // Recherche créneaux dispo sans commande
    List<LocalDateTime> slotsAvail = slotHelper.getStoreSlotAvailibility(storeId, getFakeCommands(mondayDate));

    Assertions.assertEquals(47, slotsAvail.size());

    // Vérification que les crenaux occupé ne soit pas présent dans la liste des créneaux disponible
    Assertions.assertEquals(0, slotsAvail
            .stream()
            .filter(slot->getFakeCommands(mondayDate).contains(slot))
            .toList()
            .size());
  }

  @Test
  void getStoreSlotAvailibility_with_command_after_closure(){
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(20,00,00);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Mock StoreEntity StoreEntity
    when(storeService.findStoreById(any(BigInteger.class))).thenReturn(fakeStore);

    // Mock List<LocalDateTime> SlotsWithoutConstraintByDay
    when(storeService.findStorSlotsWithoutConstraintByDay(
            any(LocalDateTime.class),
            eq(fakeStore.getFrequenceSlotTime()))
    ).thenReturn(getFakeStoreSlotsWithoutConstraintByDay(fakeStore.getFrequenceSlotTime(),mondayDate.atStartOfDay()));

    // Mock List<StoreDayScheduleEntity> storeSchedules
    when(storeService.findStoreSchedulesByDay(eq(fakeStore), any(WeekDayEntity.class))).thenReturn(getFakeStoreSchedule(weekDay, fakeStore));

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    // Recherche créneaux dispo sans commande
    List<LocalDateTime> slotsAvail = slotHelper.getStoreSlotAvailibility(storeId, getFakeCommands(mondayDate));

    Assertions.assertEquals(0, slotsAvail.size());

    // Vérification que les crenaux occupé ne soit pas présent dans la liste des créneaux disponible
    Assertions.assertEquals(0, slotsAvail
            .stream()
            .filter(slot->getFakeCommands(mondayDate).contains(slot))
            .toList()
            .size());
  }

  @Test
  void filterSlot_before_store_open() {
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Liste des créneaux dsur 24h
    List<LocalDateTime> SlotsWithoutConstraintByDay = getFakeStoreSlotsWithoutConstraintByDay(
            fakeStore.getFrequenceSlotTime(),
            mondayDate.atStartOfDay()
    );

    // Horaire commerce
    List<StoreDayScheduleEntity> storeSchedules = getFakeStoreSchedule(weekDay, fakeStore);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(8,00,00);

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    List<LocalDateTime> slotsAvailibilities = slotHelper.filterSlots(
            SlotsWithoutConstraintByDay,
            storeSchedules,
            Arrays.asList()
    );

    Assertions.assertEquals(49, slotsAvailibilities.size());
  }

  @Test
  void filterSlot_after_store_open() {
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Liste des créneaux dsur 24h
    List<LocalDateTime> SlotsWithoutConstraintByDay = getFakeStoreSlotsWithoutConstraintByDay(
            fakeStore.getFrequenceSlotTime(),
            mondayDate.atStartOfDay()
    );

    // Horaire commerce
    List<StoreDayScheduleEntity> storeSchedules = getFakeStoreSchedule(weekDay, fakeStore);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(20,00,00);

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    List<LocalDateTime> slotsAvailibilities = slotHelper.filterSlots(
            SlotsWithoutConstraintByDay,
            storeSchedules,
            Arrays.asList()
    );

    Assertions.assertEquals(0, slotsAvailibilities.size());
  }

  @Test
  void filterSlot_with_busy_slot() {
    BigInteger storeId = BigInteger.valueOf(1);
    StoreEntity fakeStore = getFakeStore(storeId);

    // Jour lundi
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate mondayDate = getNextRequestDate(1);

    // Liste des créneaux dsur 24h
    List<LocalDateTime> SlotsWithoutConstraintByDay = getFakeStoreSlotsWithoutConstraintByDay(
            fakeStore.getFrequenceSlotTime(),
            mondayDate.atStartOfDay()
    );

    // Horaire commerce
    List<StoreDayScheduleEntity> storeSchedules = getFakeStoreSchedule(weekDay, fakeStore);

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);

    // Donnée pour le SlotHelper
    int commandPrepartionTime = 15;
    LocalDateTime startOfDay = mondayDate.atStartOfDay();
    LocalDateTime endOfDay = LocalDateTime.from(startOfDay).with(LocalTime.MAX);
    LocalDateTime refTime = mondayDate.atTime(8,00,00);

    NewSlotHelper slotHelper = new NewSlotHelper(storeService);
    slotHelper.setStartOfCommandDay(startOfDay);
    slotHelper.setEndOfCommandDay(endOfDay);
    slotHelper.setCommandPreparationTime(commandPrepartionTime);
    slotHelper.setRefFilterTime(refTime);

    List<LocalDateTime> slotsAvailibilities = slotHelper.filterSlots(
            SlotsWithoutConstraintByDay,
            storeSchedules,
           getFakeCommands(mondayDate).stream().map(CommandEntity::getSlotTime).toList()
    );

    Assertions.assertEquals(47, slotsAvailibilities.size());
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  StoreEntity getFakeStore(BigInteger storeId) {
    StoreEntity store = new StoreEntity();
    store.setId(storeId);
    store.setFrequenceSlotTime(10);
    return store;
  }

  /**
   * Horaire commerce sur une journée
   * @param day WeekDayEntity
   * @return List<StoreDayScheduleEntity>
   */
  List<StoreDayScheduleEntity> getFakeStoreSchedule(WeekDayEntity day, StoreEntity store) {
    StoreDayScheduleEntity storeSchedule = new StoreDayScheduleEntity();
    storeSchedule.setWeekDay(day);
    storeSchedule.setStore(store);
    storeSchedule.setOpeningTime(LocalTime.of(8,30, 0));
    storeSchedule.setClosingTime(LocalTime.of(17,0,0));

    return Arrays.asList(storeSchedule);
  }

  /**
   * Horaire commerce sur une journée avec 2 créneaux
   * @param day WeekDayEntity
   * @return List<StoreDayScheduleEntity>
   */
  List<StoreDayScheduleEntity> getFakeDoubleStoreSchedule(WeekDayEntity day, StoreEntity store) {
    StoreDayScheduleEntity storeScheduleMorning = new StoreDayScheduleEntity();
    storeScheduleMorning.setWeekDay(day);
    storeScheduleMorning.setStore(store);
    storeScheduleMorning.setOpeningTime(LocalTime.of(8,30, 0));
    storeScheduleMorning.setClosingTime(LocalTime.of(13,0,0));

    StoreDayScheduleEntity storeScheduleAfternoon = new StoreDayScheduleEntity();
    storeScheduleAfternoon.setWeekDay(day);
    storeScheduleAfternoon.setStore(store);
    storeScheduleAfternoon.setOpeningTime(LocalTime.of(14,30, 0));
    storeScheduleAfternoon.setClosingTime(LocalTime.of(20,0,0));



    return Arrays.asList(storeScheduleMorning, storeScheduleAfternoon);
  }

  /**
   * Génération dune liste de créneaux disponible sans contrainte sur 24h00
   * @param frequenceSlot Integer - Frequence des commande
   * @param startOfCommandDay LocalDateTime - Debut de journée
   * @return List<LocalDateTime>
   */
  List<LocalDateTime> getFakeStoreSlotsWithoutConstraintByDay(Integer frequenceSlot, LocalDateTime startOfCommandDay) {

    Integer slotAvailibilityInOneDay = calculateNumberOfCommandSlotForOneDay(frequenceSlot);

    // Génération d'une liste de créneaux sur 24h ne prenant pas en compte les contraintes
    return Stream.iterate(startOfCommandDay, dateTime-> dateTime.plusMinutes(frequenceSlot))
      .limit(slotAvailibilityInOneDay)
      .toList();
  }

  /**
   * Fausse commandes
   * @param date LocalDate - Date des commandes
   * @return  List<CommandEntity>
   */
  private List<CommandEntity> getFakeCommands(LocalDate date) {
    CommandEntity command1 = new CommandEntity();
    CommandEntity command2 = new CommandEntity();

    command1.setPreparationTime(15);
    command1.setSlotTime(date.atTime(12,20,00));

    command2.setPreparationTime(10);
    command2.setSlotTime(date.atTime(12,40,00));

    return Arrays.asList(command1, command2);
  }

  /**
   * Calcul du nombre de Slot disponible dans une journée
   * @param storeFrequenceSlot Integer - Fréquence des commandes
   * @return Inetegr - Nombre de créneaux disponible
   */
  private Integer calculateNumberOfCommandSlotForOneDay(Integer storeFrequenceSlot) {
    //todo faire test unitaire

    // Nombre de minute dans 1h
    final int MINUTES_IN_HOUR = 60;

    // Nombre d' heure dans 1 journée
    final int HOUR_IN_DAY = 24;

    // Nombre d'itération de commande par heure
    final int ITERATION_PER_HOUR = MINUTES_IN_HOUR / storeFrequenceSlot;

    // Nombre d'iteration pour 24h
    return ITERATION_PER_HOUR * HOUR_IN_DAY;
  }

  /**
   * Calcul la date du prochain jour demandé
   * @param targetWeekDayNumber Integer - N° du jour demandé
   * @return LocalDate
   */
  private LocalDate getNextRequestDate(int targetWeekDayNumber) {
    LocalDate refDate = LocalDate.now();
    return Stream.iterate(refDate, date-> date.plusDays(1))
            .limit(7)
            .filter(date-> date.getDayOfWeek().getValue() == targetWeekDayNumber)
            .findFirst()
            .orElse(null);
  }

}
