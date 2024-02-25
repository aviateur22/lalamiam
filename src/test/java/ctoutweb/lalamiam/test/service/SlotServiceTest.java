//package ctoutweb.lalamiam.test.service;
//
//import ctoutweb.lalamiam.helper.StoreScheduleHelper;
//import ctoutweb.lalamiam.model.dto.FindListOfSlotTimeAvailableDto;
//import ctoutweb.lalamiam.model.dto.ProInformationDto;
//import ctoutweb.lalamiam.repository.CommandRepository;
//import ctoutweb.lalamiam.repository.entity.StoreEntity;
//import ctoutweb.lalamiam.service.*;
//import helper.*;
//import jakarta.annotation.PostConstruct;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@SpringBootTest
//public class SlotServiceTest {
//
//  @Autowired
//  StoreScheduleHelper storeScheduleHelper;
//  @Autowired
//  CommandRepository commandRepository;
//  @Autowired
//  StoreService storeService;
//  @Autowired
//  SlotService slotService;
//  @Autowired
//  ProService proService;
//  @Autowired
//  CommandService commandService;
//  @Autowired
//  ProductService productService;
//  CommandHelper commandHelper;
//  StoreHelper storeHelper;
//  SlotHelper slotHelper;
//  ProHelper proHelper;
//  ProductHelper productHelper;
//
//  @PostConstruct
//  void init() {
//    storeHelper = new StoreHelper(storeService);
//    slotHelper = new SlotHelper(commandRepository);
//    proHelper =  new ProHelper(proService);
//    productHelper = new ProductHelper(productService);
//    commandHelper = new CommandHelper(productHelper, commandRepository, commandService);
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avant ouvertue du commerce
//   */
//  @Test
//  void should_find_list_of_slot_available_before_opening_time() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    LocalDateTime today = LocalDateTime.now().plusDays(1);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY).toLocalDate();
//
//    // Horaire d'accés à la page création d'une commande
//    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
//    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 5;
//
//    // Récupération des horaire de la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Calcul du nombre de slot disponible avant ouverture du restaurant
//    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateBeforeOpeningTime
//    );
//
//    // Calcul du nombre de slot disponible avant ouverture du restaurant
//    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateBeforeOpeningTime2
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateBeforeOpeningTime)
//
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime2 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateBeforeOpeningTime2)
//
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableBeforeOpeningTime.size());
//    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableBeforeOpeningTime2.size());
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
//   */
//  @Test
//  void should_find_list_of_slot_available_during_opening_time() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
//    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 5;
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime1
//    );
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime2
//    );
//
//    // Recherche des slots diponibles
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime1 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime1)
//    );
//
//    // Recherche des slots diponibles
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime2 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime2)
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableDuringOpeningTime1.size());
//    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableDuringOpeningTime2.size());
//
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
//   */
//  @Test
//  void should_find_list_of_slot_available_when_store_close() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 5;
//
//    // Schedules commerce de toutes la semaine
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avant ouvertue du commerce
//   */
//  @Test
//  void should_find_list_of_slot_available_before_opening_time_preparation_time_bigger_than_frequence_command() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Date de consultation des Slot
//    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
//    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Calcul du nombre de slot disponible avant ouverture du restaurant
//    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateBeforeOpeningTime
//    );
//
//    // Calcul du nombre de slot disponible avant ouverture du restaurant
//    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateBeforeOpeningTime2
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateBeforeOpeningTime)
//
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime2 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateBeforeOpeningTime2)
//
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableBeforeOpeningTime.size());
//    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableBeforeOpeningTime2.size());
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
//   */
//  @Test
//  void should_find_list_of_slot_available_during_opening_time_bigger_than_frequence_command() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    // Création Store
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
//    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime1
//    );
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime2
//    );
//
//    // Recherche des slots diponibles
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime1 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime1)
//    );
//
//    // Recherche des slots diponibles
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime2 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime2)
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableDuringOpeningTime1.size());
//    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableDuringOpeningTime2.size());
//
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
//   */
//  @Test
//  void should_find_list_of_slot_available_when_store_close_bigger_than_frequence_command() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de consultation
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avant ouvertue du commerce
//   */
//  @Test
//  void should_find_list_of_slot_available_before_opening_time_preparation_time_with_commands_avail_in_day() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Date de consultation des Slot
//    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
//    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Génération de commande
//    commandHelper.createCommands(commandDate, 5, store);
//
//    // Calcul du nombre de slot disponible avant ouverture du restaurant
//    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateBeforeOpeningTime
//    );
//
//    // Calcul du nombre de slot disponible avant ouverture du restaurant
//    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateBeforeOpeningTime2
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateBeforeOpeningTime)
//
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime2 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateBeforeOpeningTime2)
//
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableBeforeOpeningTime.size());
//    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableBeforeOpeningTime2.size());
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
//   */
//  @Test
//  void should_find_list_of_slot_available_during_opening_time_with_commands_avail_in_day() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    // Horaire Consultation
//    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
//    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime1
//    );
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime2
//    );
//
//    // Recherche des slots diponibles
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime1 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime1)
//    );
//
//    // Recherche des slots diponibles
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime2 = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime2)
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableDuringOpeningTime1.size());
//    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableDuringOpeningTime2.size());
//
//  }
//
//  /**
//   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
//   */
//  @Test
//  void should_find_list_of_slot_available_when_store_close_with_commands_avail_in_day() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int TUESDAY_DAY_WEEK_NUMBER = 2;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//
//  }
//
//  /**
//   * Recherche slot disponible le lundi avant ouverture commerce
//   */
//  @Test
//  void should_find_slot_available_on_monday_before_store_is_open() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int MONDAY_DAY_WEEK_NUMBER = 1;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(MONDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, MONDAY_DAY_WEEK_NUMBER);
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(8, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//  }
//
//  /**
//   * Recherche slot disponible le lundi avant ouverture commerce
//   */
//  @Test
//  void should_find_slot_available_on_monday_during_store_open_time() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int MONDAY_DAY_WEEK_NUMBER = 1;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(MONDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, MONDAY_DAY_WEEK_NUMBER);
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(19, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//  }
//
//  /**
//   * Recherche slot disponible le lundi magasin fermé
//   */
//  @Test
//  void should_find_slot_available_on_monday_when_store_is_close() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int MONDAY_DAY_WEEK_NUMBER = 1;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(MONDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, MONDAY_DAY_WEEK_NUMBER);
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//  }
//
//  /**
//   * Recherche slot disponible le dimanche
//   */
//  @Test
//  void should_find_slot_available_on_sunday_before_open_time() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int SUNDAY_DAY_WEEK_NUMBER = 1;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(SUNDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, SUNDAY_DAY_WEEK_NUMBER);
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(10, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//  }
//
//  /**
//   * Recherche slot disponible le dimanche durant l'ouverture
//   */
//  @Test
//  void should_find_slot_available_on_sunday_during_open_time() {
//    // Creation store - produit - commande
//    ProInformationDto pro = proHelper.createPro();
//
//    StoreEntity store = storeHelper.createStore(pro);
//
//    // Jour de commande
//    int SUNDAY_DAY_WEEK_NUMBER = 1;
//
//    // Date de la commande
//    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(SUNDAY_DAY_WEEK_NUMBER, DayReference.TODAY));
//
//    // Horaire de la commerce sur la journée
//    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, SUNDAY_DAY_WEEK_NUMBER);
//
//    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(12, 9,50);
//
//    //Temps de prépa commande
//    int preparationTime = 15;
//
//    // Calcul du nombre de slot disponible
//    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
//            dailyStoreSchedules,
//            store,
//            preparationTime,
//            consultationDateDuringOpeningTime
//    );
//
//    // Recherche des slots diponible avant ouverture du commerce
//    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = slotService.findAllSlotAvailable(
//            new FindListOfSlotTimeAvailableDto(
//                    commandDate,
//                    store.getId(),
//                    preparationTime,
//                    consultationDateDuringOpeningTime)
//    );
//
//
//    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
//  }
//
//
//}
//
