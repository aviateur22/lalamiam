package ctoutweb.lalamiam.test.service;

import ctoutweb.lalamiam.helper.StoreScheduleHelper;
import ctoutweb.lalamiam.model.DailyStoreSchedule;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.builder.StoreEntityBuilder;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.service.CommandService;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.ProductService;
import ctoutweb.lalamiam.service.StoreService;
import helper.*;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO vérifier qu'une commande ne puisse pas etre supprimé ou modifié par un store exterieur
@SpringBootTest
public class CommandServiceTest {

  @Autowired
  ProRepository proRepository;

  @Autowired
  CommandService commandService;

  @Autowired
  CommandRepository commandRepository;
  @Autowired
  ProService proService;

  @Autowired
  CommandProductRepository cookRepository;
  @Autowired
  ProductService productService;

  @Autowired
  StoreScheduleHelper storeScheduleHelper;

  @Autowired
  StoreService storeService;

  StoreHelper storeHelper;
  CommandHelper commandHelper =  new CommandHelper();

  SlotHelper slotHelper;

  HashMap<String, Boolean> testParameters = new HashMap<>();

  //Liste pour commande Pro1
  List<ProductWithQuantity> productsInCommand = new ArrayList<>();
  StoreEntity store;

  @PostConstruct
  void init() {
    storeHelper = new StoreHelper(storeService);
    slotHelper = new SlotHelper(commandRepository);
  }
  @BeforeEach
  void beforeEach() {
    commandRepository.deleteAll();
    proRepository.truncateAll();

    testParameters.clear();
    testParameters.put("isStoreExist", true);
    testParameters.put("isPhoneClientExist", true);
    testParameters.put("isSlotTimeValid", true);
    testParameters.put("isCommandWithProduct", true);
    testParameters.put("isProdutcInStore", true);
    testParameters.put("isProdutcBelongToStore", true);
  }
  @Test
  void should_create_command() {
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Controle de la commande
    Assertions.assertEquals(1, commandRepository.countAll());
    //Assertions.assertEquals(addCommand.phoneClient(), addCommand.getClientPhone());
    //Assertions.assertEquals(addCommand.getPreparationTime(), addCommand.getPreparationTime());

    // Controle relation produits - store - commande
    Assertions.assertEquals(3, cookRepository.countAll());
    List<CommandProductEntity> commandProducts = cookRepository.findAll();

    Assertions.assertEquals(productsInCommand.get(0).getProductId(), commandProducts.get(0).getProduct().getId());
    Assertions.assertEquals(addCommand.commandId(), commandProducts.get(0).getCommand().getId());
    //Assertions.assertEquals(addCommand  store.getId(), commandProducts.get(0).getStore().getId());

    Assertions.assertEquals(productsInCommand.get(1).getProductId(), commandProducts.get(1).getProduct().getId());
    Assertions.assertEquals(addCommand.commandId(), commandProducts.get(1).getCommand().getId());
    //Assertions.assertEquals(store.getId(), commandProducts.get(1).getStore().getId());

    Assertions.assertEquals(productsInCommand.get(2).getProductId(), commandProducts.get(2).getProduct().getId());
    Assertions.assertEquals(addCommand.commandId(), commandProducts.get(2).getCommand().getId());
    //Assertions.assertEquals(store.getId(), commandProducts.get(2).getStore().getId());
  }

  @Test
  void should_not_create_command_if_store_not_exist() {
    testParameters.put("isStoreExist", false);
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.addCommand(addCommandSchema()));
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.addCommand(addCommandSchema()));
    Assertions.assertEquals("Le commerce n'existe pas", exception.getMessage());
  }

  @Test
  void should_not_create_command_if_phone_client_missing() {
    testParameters.put("isPhoneClientExist", false);
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.addCommand(addCommandSchema()));
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.addCommand(addCommandSchema()));
    Assertions.assertEquals("Le téléphone client est obligatoire", exception.getMessage());
  }

  @Test
  void should_not_create_command_if_slot_time_invalid() {
    testParameters.put("isSlotTimeValid", false);
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.addCommand(addCommandSchema()));
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.addCommand(addCommandSchema()));
    Assertions.assertEquals("La commande ne peut pas être dans le passée", exception.getMessage());
  }

  @Test
  void should_not_create_command_if_command_empty() {
    testParameters.put("isCommandWithProduct", false);
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.addCommand(addCommandSchema()));
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.addCommand(addCommandSchema()));
    Assertions.assertEquals("La commande ne peut pas être vide", exception.getMessage());

  }

  @Test
  void should_not_create_command_if_product_not_register() {
    testParameters.put("isProdutcInStore", false);
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.addCommand(addCommandSchema()));
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.addCommand(addCommandSchema()));
    Assertions.assertEquals("Le produit n'existe pas", exception.getMessage());

  }

  @Test
  void should_not_create_command_if_product_not_belong_to_store() {
    testParameters.put("isProdutcBelongToStore", false);
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.addCommand(addCommandSchema()));
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.addCommand(addCommandSchema()));
    Assertions.assertEquals("Certains produits à ajouter ne sont pas rattachés au commerce", exception.getMessage());
  }

  @Test
  void should_update_product_quantity_in_command(){
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation d'un produit de la commande
    BigInteger productChangeId = productsInCommand.get(0).getProductId();

    // Modification du produit dans la commande
    UpdateProductQuantityDto updateCommandSchema = new UpdateProductQuantityDto(
            productChangeId, addCommand.commandId(), store.getId(), 4);
    UpdateProductQuantityResponseDto productUpdated = commandService.updateProductQuantityInCommand(updateCommandSchema);

    Assertions.assertEquals(4, productUpdated.productInCommand().getProductQuantity());

    // Vérification id
    Assertions.assertEquals(productChangeId, productUpdated.productInCommand().getProductId());

    // Vérification du nouveau prix
    Assertions.assertEquals(140, productUpdated.commandPrice());

    // Verification du nouveau temps de prepapration de la commande
    Assertions.assertEquals(80, productUpdated.commandPreparationTime());
  }

  @Test
  void should_not_update_product_quantity_if_product_not_belong_to_command() {
    // Creation Commande store1
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Creation produit pour store2
    ProInformationDto pro2 = createPro();
    StoreEntity store2 = storeHelper.createStore(pro2);
    List<AddProductResponseDto> productStore2 = createProduct(store2.getId());

    // Récuperation d'un produit du store2
    BigInteger productStore2Id = productStore2.get(0).id();

    // Modification quantité commande store1 avec un produit du store2
    UpdateProductQuantityDto updateCommandSchema = new UpdateProductQuantityDto(
            productStore2Id, addCommand.commandId(), store.getId(), 12);

    Exception exception = Assertions.assertThrows(
            RuntimeException.class,
            ()->commandService.updateProductQuantityInCommand(updateCommandSchema)
    );
    Assertions.assertEquals("Certains produits à modifier ne sont pas rattachés au commerce", exception.getMessage());
  }

  @Test
  void should_force_update_product_quantity_to_1_if_quantity_inferior_at_1() {
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation d'un produit de la commande
    BigInteger productChangeId = productsInCommand.get(0).getProductId();

    // Modification du produit dans la commande
    UpdateProductQuantityDto updateCommandSchema = new UpdateProductQuantityDto(
            productChangeId, addCommand.commandId(), store.getId(), 0);
    UpdateProductQuantityResponseDto productUpdated = commandService.updateProductQuantityInCommand(updateCommandSchema);

    Assertions.assertEquals(1, productUpdated.productInCommand().getProductQuantity());
    Assertions.assertEquals(productChangeId, productUpdated.productInCommand().getProductId());
  }

  @Test
  void should_delete_one_product_in_command() {
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Recuperation du produit a supprimer - Prix 10€ - temps prepaparation 5 min
    BigInteger productId = addCommand.productInCommandList().get(0).getProductId();

    // Produits restants
    // Quantite 2 - AddProductSchema addProductSchema2 = new AddProductSchema("coco", 20D, "initial description", 10, "s", storeId);
    // Quantite 2 - AddProductSchema addProductSchema1 = new AddProductSchema("lait", 10D, "initial description", 5, "s", storeId);

    BigInteger commandId = addCommand.commandId();
    // Suppression du produit
    DeleteProductInCommandDto deleteProductInCommand = new DeleteProductInCommandDto(commandId, productId, store.getId());
    SimplifyCommandDetailResponseDto updateCommandDetail = commandService.deleteProductInCommand(deleteProductInCommand);

    // Vérification nombre de produit
    Assertions.assertEquals(2, updateCommandDetail.productInCommandList().size());

    // Vérification produit supprimé absent
    Assertions.assertEquals(0,
            updateCommandDetail.productInCommandList()
                    .stream()
                    .filter(product -> product.getProductId() == productId)
                    .collect(Collectors.toList())
                    .size());

    // Vérification du nouveau prix
    Assertions.assertEquals(100, updateCommandDetail.commandPrice());

    // Verification du nouveau temps de prepapration de la commande
    Assertions.assertEquals(60, updateCommandDetail.commandPreparationTime());
  }

  @Test
  void should_not_add_product_products_already_in_command() {
    // TODO 1 commande ne oeut pas etre modifié si pas existante
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation produitId a ajouter
    List<ProductWithQuantity> productWithQuantityList = new ArrayList<>();
    BigInteger productId = productsInCommand.get(0).getProductId();
    productWithQuantityList.add(new ProductWithQuantity(productId, 2));
    AddProductsInCommandDto addProductsInCommandSchema = new AddProductsInCommandDto(store.getId(), productWithQuantityList, addCommand.commandId());

    // Exception
    Exception exception = Assertions.assertThrows(RuntimeException.class,  ()->commandService.addProductsInCommand(addProductsInCommandSchema));

    // Message de l'ecxeption
    Assertions.assertEquals(String.format("Le produit ayant l'identifiant %s est déja dans la commande", productId), exception.getMessage());
  }

  @Test
  void should_add_one_products_with_quantity_2_in_command()  {
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation produitId a ajouter
    List<ProductWithQuantity> productWithQuantityList = new ArrayList<>();
    BigInteger productId = productsInCommand.get(0).getProductId().add(BigInteger.valueOf(3));

    productWithQuantityList.add(new ProductWithQuantity(productId, 2));
    AddProductsInCommandDto addProductsInCommandSchema = new AddProductsInCommandDto(store.getId(), productWithQuantityList, addCommand.commandId());
    AddProductsInCommandResponseDto commandDetail = commandService.addProductsInCommand(addProductsInCommandSchema);

    // Vérification que le produit ajouté n'est pas en doublons
    List<ProductWithQuantity> findProductIdInProductList = commandDetail
            .addProducts()
            .stream()
            .filter(product-> product.getProductId().equals(productId)).collect(Collectors.toList());
    Assertions.assertEquals(1, findProductIdInProductList.size());

    // Qiantité du nouveau produit
    Assertions.assertEquals(2, findProductIdInProductList
            .stream()
            .filter(product->product.getProductId().equals(productId))
            .findFirst()
            .get()
            .getProductQuantity());

    // Vérification nombre de produit total dans la commande
    Assertions.assertEquals(8, commandDetail.numberOProductInCommand());

    // Vérification du nouveau prix
    Assertions.assertEquals(140, commandDetail.commandPrice());

    // Verification du nouveau temps de prepapration de la commande
    Assertions.assertEquals(80, commandDetail.commandPreparationTime());
  }

  /**
   * Recherche des slots diponible pour une commande avant ouvertue du commerce
   */
  @Test
  void should_find_list_of_slot_available_before_opening_time() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY).toLocalDate();

    // Horaire d'accés à la page création d'une commande
    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);

    //Temps de prépa commande
    int preparationTime = 5;

    // Récupération des horaire de la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime
    );

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime2
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateBeforeOpeningTime)

    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime2 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateBeforeOpeningTime2)

    );

    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableBeforeOpeningTime.size());
    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableBeforeOpeningTime2.size());
  }

  /**
   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
   */
  @Test
  void should_find_list_of_slot_available_during_opening_time() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
    int preparationTime = 5;

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime1
    );

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime2
    );

    // Recherche des slots diponibles
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime1 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime1)
    );

    // Recherche des slots diponibles
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime2 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime2)
    );

    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableDuringOpeningTime1.size());
    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableDuringOpeningTime2.size());

  }

  /**
   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
   */
  @Test
  void should_find_list_of_slot_available_when_store_close() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    //Temps de prépa commande
    int preparationTime = 5;

    // Schedules commerce de toutes la semaine
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );

    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());

  }

  /**
   * Recherche des slots diponible pour une commande avant ouvertue du commerce
   */
  @Test
  void should_find_list_of_slot_available_before_opening_time_preparation_time_bigger_than_frequence_command() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Date de consultation des Slot
    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime
    );

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime2
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateBeforeOpeningTime)

    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime2 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateBeforeOpeningTime2)

    );

    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableBeforeOpeningTime.size());
    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableBeforeOpeningTime2.size());
  }

  /**
   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
   */
  @Test
  void should_find_list_of_slot_available_during_opening_time_bigger_than_frequence_command() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    // Création Store
    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime1
    );

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime2
    );

    // Recherche des slots diponibles
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime1 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime1)
    );

    // Recherche des slots diponibles
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime2 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime2)
    );

    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableDuringOpeningTime1.size());
    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableDuringOpeningTime2.size());

  }

  /**
   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
   */
  @Test
  void should_find_list_of_slot_available_when_store_close_bigger_than_frequence_command() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de consultation
    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );

    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());

  }

  /**
   * Recherche des slots diponible pour une commande avant ouvertue du commerce
   */
  @Test
  void should_find_list_of_slot_available_before_opening_time_preparation_time_with_commands_avail_in_day() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Date de consultation des Slot
    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Génération de commande
    createCommands(commandDate, 5, store);

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime
    );

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime2
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateBeforeOpeningTime)

    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableBeforeOpeningTime2 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateBeforeOpeningTime2)

    );

    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableBeforeOpeningTime.size());
    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableBeforeOpeningTime2.size());
  }

  /**
   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
   */
  @Test
  void should_find_list_of_slot_available_during_opening_time_with_commands_avail_in_day() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    // Horaire Consultation
    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest1 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime1
    );

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest2 = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime2
    );

    // Recherche des slots diponibles
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime1 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime1)
    );

    // Recherche des slots diponibles
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime2 = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime2)
    );

    Assertions.assertEquals(findSlotAvailableTest1.size(), findAllSlotAvailableDuringOpeningTime1.size());
    Assertions.assertEquals(findSlotAvailableTest2.size(), findAllSlotAvailableDuringOpeningTime2.size());

  }

  /**
   * Recherche des slots diponible pour une commande avec 2 horaires d'ouverture
   */
  @Test
  void should_find_list_of_slot_available_when_store_close_with_commands_avail_in_day() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int TUESDAY_DAY_WEEK_NUMBER = 2;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(TUESDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, TUESDAY_DAY_WEEK_NUMBER);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );

    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());

  }

  /**
   * Recherche slot disponible le lundi avant ouverture commerce
   */
  @Test
  void should_find_slot_available_on_monday_before_store_is_open() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int MONDAY_DAY_WEEK_NUMBER = 1;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(MONDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, MONDAY_DAY_WEEK_NUMBER);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(8, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );


    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
  }

  /**
   * Recherche slot disponible le lundi avant ouverture commerce
   */
  @Test
  void should_find_slot_available_on_monday_during_store_open_time() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int MONDAY_DAY_WEEK_NUMBER = 1;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(MONDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, MONDAY_DAY_WEEK_NUMBER);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );


    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
  }

  /**
   * Recherche slot disponible le lundi magasin fermé
   */
  @Test
  void should_find_slot_available_on_monday_when_store_is_close() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int MONDAY_DAY_WEEK_NUMBER = 1;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(MONDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, MONDAY_DAY_WEEK_NUMBER);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );


    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
  }

  /**
   * Recherche slot disponible le dimanche
   */
  @Test
  void should_find_slot_available_on_sunday_before_open_time() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int SUNDAY_DAY_WEEK_NUMBER = 1;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(SUNDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, SUNDAY_DAY_WEEK_NUMBER);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(10, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );


    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
  }

  /**
   * Recherche slot disponible le dimanche durant l'ouverture
   */
  @Test
  void should_find_slot_available_on_sunday_during_open_time() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    StoreEntity store = storeHelper.createStore(pro);

    // Jour de commande
    int SUNDAY_DAY_WEEK_NUMBER = 1;

    // Date de la commande
    LocalDate commandDate = LocalDate.from(commandHelper.getDateOfDay(SUNDAY_DAY_WEEK_NUMBER, DayReference.TODAY));

    // Horaire de la commerce sur la journée
    var dailyStoreSchedules = storeScheduleHelper.getDayStoreSchedule(store, SUNDAY_DAY_WEEK_NUMBER);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(12, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = slotHelper.findSlotAvail(
            dailyStoreSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime
    );

    // Recherche des slots diponible avant ouverture du commerce
    List<LocalDateTime> findAllSlotAvailableDuringOpeningTime = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDateDuringOpeningTime)
    );


    Assertions.assertEquals(findSlotAvailableTest.size(), findAllSlotAvailableDuringOpeningTime.size());
  }

  /**
   * Création schema pour une commande
   * @return AddCommandSchema
   */
  private AddCommandDto addCommandSchema() {

    // Parametrages des tests
    boolean isStoreExist = testParameters.get("isStoreExist");
    boolean isPhoneClientExist = testParameters.get("isPhoneClientExist");
    boolean isSlotTimeValid = testParameters.get("isSlotTimeValid");
    boolean isCommandWithProduct = testParameters.get("isCommandWithProduct");
    boolean isProductInStore = testParameters.get("isProdutcInStore");
    boolean isProdutcBelongToStore = testParameters.get("isProdutcBelongToStore");

    // Creation Pro
    ProInformationDto createdPro = createPro();
    ProInformationDto createdPro2;

    // Creation Store
    StoreEntity createdStore = storeHelper.createStore(createdPro);
    StoreEntity createdStore2;

    store = isStoreExist ?
            createdStore :
            StoreEntityBuilder.aStoreEntity().withId(BigInteger.valueOf(0)).build();

    // Création produits
    List<AddProductResponseDto> createProductList = createProduct(createdStore.getId());

    // Creation commande
    createProductsInCommand(createProductList);

    // Ajout de produit non present dans la commande
    createProductAfetrCommands(createdStore.getId());

    // Creation Pro2 + Store2 + produits2 et ajout d'un produit à la commande 1
    if(!isProdutcBelongToStore) {
      createdPro2 = createPro();
      createdStore2 = storeHelper.createStore(createdPro2);
      List<AddProductResponseDto> createProductList2 = createProduct(createdStore2.getId());
      ProductWithQuantity productStore2 = new ProductWithQuantity(createProductList2.get(0).id(), 1);
      productsInCommand.add(productStore2);
    }

    // Création commande
    String phoneCient = isPhoneClientExist ?
            "0623274102" :
            "";
    LocalDateTime commandSlotTime = isSlotTimeValid ?
            LocalDateTime.now().plusHours(1) :
            LocalDateTime.of(2022, 10,10,23,55,10);


    if(!isCommandWithProduct) productsInCommand.clear();

    if(!isProductInStore) productsInCommand.add(new ProductWithQuantity(BigInteger.valueOf(0), 1));

    AddCommandDto addCommandSchema = new AddCommandDto(
            phoneCient,
            commandSlotTime,
            store.getId(),
            productsInCommand);

    // schema ajout commande
    return addCommandSchema;
  }

  private AddCommandDto customCommandSchema(BigInteger storeId, LocalDateTime slotTime) {
    return new AddCommandDto(
            "phoneCient",
            slotTime,
            storeId,
            productsInCommand);
  }
  /**
   * Liste des produits pour une commande
   * @param productList
   * @return
   */
  public List<ProductWithQuantity> createProductsInCommand(List<AddProductResponseDto> productList) {
    productsInCommand = productList
            .stream()
            .map(product -> new ProductWithQuantity(product.id(), 2))
            .collect(Collectors.toList());

    return productsInCommand;
  }


  /**
   * Creation Professionnel
   * @return ProInformationDto
   */
  public ProInformationDto createPro() {
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));
    return createdPro;
  }

  public List<AddProductResponseDto> createProduct(BigInteger storeId) {
    AddProductDto addProductSchema1 = new AddProductDto("lait", 10D, "initial description", 5, "s", storeId);
    AddProductDto addProductSchema2 = new AddProductDto("coco", 20D, "initial description", 10, "s", storeId);
    AddProductDto addProductSchema3 = new AddProductDto("orange", 30D, "initial description", 20, "s", storeId);

    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);

    List<AddProductResponseDto> createdProductList = new ArrayList<>();
    createdProductList.add(addProduct1);
    createdProductList.add(addProduct2);
    createdProductList.add(addProduct3);

    return createdProductList;
  }

  public List<AddProductResponseDto> createProductAfetrCommands(BigInteger storeId) {
    AddProductDto addProductSchema1 = new AddProductDto("pain", 10D, "initial description", 5, "s", storeId);
    AddProductDto addProductSchema2 = new AddProductDto("beurre", 20D, "initial description", 10, "s", storeId);
    AddProductDto addProductSchema3 = new AddProductDto("miel", 30D, "initial description", 20, "s", storeId);

    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);

    List<AddProductResponseDto> createdProductList = new ArrayList<>();
    createdProductList.add(addProduct1);
    createdProductList.add(addProduct2);
    createdProductList.add(addProduct3);

    return createdProductList;
  }


  public void createCommands(LocalDate commandDate, int numberOfCommands, StoreEntity store) {

    // Génreation de produits
    List<AddProductResponseDto> createProductList = createProduct(store.getId());

    // Création d'une liste de produits
    createProductsInCommand(createProductList);


    Random random = new Random();

    Stream.iterate(1, n -> n+1).limit(numberOfCommands).forEach(n->{
      int morningHour = random.nextInt(11 ,14) + 1;
      int afternoonHour = random.nextInt(18, 21) + 1;
      int minute = (random.nextInt(6)) * 10;
      int second = 0;

      LocalTime commandTimeMorning = LocalTime.of(morningHour, minute, second);
      LocalTime commandTimeAfternoon = LocalTime.of(afternoonHour, minute, second);

      LocalDateTime commandDateTimeMorning = LocalDateTime.of(
              commandDate.getYear(),
              commandDate.getMonth(),
              commandDate.getDayOfMonth(),
              commandTimeMorning.getHour(),
              commandTimeMorning.getMinute(),
              commandTimeMorning.getSecond()
      );

      LocalDateTime commandDateTimeAfternoon = LocalDateTime.of(
              commandDate.getYear(),
              commandDate.getMonth(),
              commandDate.getDayOfMonth(),
              commandTimeAfternoon.getHour(),
              commandTimeAfternoon.getMinute(),
              commandTimeAfternoon.getSecond()
      );

      if(!commandRepository.findCommandBySlotTimeAndStore(commandDateTimeMorning, store).isPresent())
        commandService.addCommand(customCommandSchema(store.getId(), commandDateTimeMorning));

      if(!commandRepository.findCommandBySlotTimeAndStore(commandDateTimeAfternoon, store).isPresent())
        commandService.addCommand(customCommandSchema(store.getId(), commandDateTimeAfternoon));
    });

  }

}
