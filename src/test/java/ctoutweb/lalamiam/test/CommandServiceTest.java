package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.StoreSchedule;
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
import ctoutweb.lalamiam.test.helper.CommonFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.Duration;
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
  StoreService storeService;
  HashMap<String, Boolean> testParameters = new HashMap<>();

  //Liste pour commande Pro1
  List<ProductWithQuantity> productsInCommand = new ArrayList<>();
  StoreEntity store;

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
    StoreEntity store2 = createStore(pro2, null);
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
  void should_add_one_or_multiple_products_to_command() {
    // TODO 1 commande ne oeut pas etre modifié si pas existante
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation produitId a ajouter
    List<BigInteger> productIdList = new ArrayList<>();
    BigInteger productId = productsInCommand.get(0).getProductId();
    productIdList.add(productId);
    AddProductsInCommandDto addProductsInCommandSchema = new AddProductsInCommandDto(store.getId(), productIdList, addCommand.commandId());
    SimplifyCommandDetailResponseDto commandDetail = commandService.addProductsInCommand(addProductsInCommandSchema);

    // Vérification nombre de produit
    Assertions.assertEquals(7, commandDetail.numberOProductInCommand());

    // Vérification du nouveau prix
    Assertions.assertEquals(130, commandDetail.commandPrice());

    // Verification du nouveau temps de prepapration de la commande
    Assertions.assertEquals(75, commandDetail.commandPreparationTime());

  }

  @Test
  void should_add_one_product_already_in_command() {
    // Creation Commande
    CompleteCommandDetailResponseDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation produitId a ajouter
    List<BigInteger> productIdList = new ArrayList<>();
    BigInteger productId = productsInCommand.get(0).getProductId();

    productIdList.add(productId);
    AddProductsInCommandDto addProductsInCommandSchema = new AddProductsInCommandDto(store.getId(), productIdList, addCommand.commandId());
    SimplifyCommandDetailResponseDto commandDetail = commandService.addProductsInCommand(addProductsInCommandSchema);

    // Vérification que le produit ajouté n'est pas en doublons
    List<ProductWithQuantity> findProductIdInProductList = commandDetail
            .productInCommandList()
            .stream()
            .filter(product-> product.getProductId().equals(productId)).collect(Collectors.toList());
    Assertions.assertEquals(1, findProductIdInProductList.size());
    Assertions.assertEquals(3, findProductIdInProductList.stream().filter(product->product.getProductId().equals(productId)).findFirst().get().getProductQuantity());
  }

  @Test
  void should_find_list_of_slot_available_for_store_with_1_schedules_in_day() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            )
    );
    StoreEntity store = createStore(pro, storeSchedules);
    List<AddProductResponseDto> createProductList = createProduct(store.getId());
    List<ProductWithQuantity> products = createProductsInCommand(createProductList);

    LocalDateTime tomorrow = LocalDateTime.now().plusDays(2);
    LocalDateTime now = LocalDateTime.now().plusDays(1);
    LocalDateTime yesterday = LocalDateTime.now();

    // Creation Commande du soir
    LocalDateTime time1 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),20,55,00);
    LocalDateTime time2 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),19,00,00);
    LocalDateTime time3 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),20,30,00);
    LocalDateTime time4 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),18,30,00);
    LocalDateTime time5 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),19,30,00);

    // Creation Commande du  matin
    int mornigCommand = 4;
    LocalDateTime time6 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),12,30,00);
    LocalDateTime time7 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),12,20,00);
    LocalDateTime time8 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),12,00,00);
    LocalDateTime time9 = LocalDateTime.of(now.getYear(), now.getMonth() ,now.getDayOfMonth(),12,40,00);

    // OldCommannd
    LocalDateTime timeOld = LocalDateTime.of(yesterday.getYear(), yesterday.getMonth() ,yesterday.getDayOfMonth(),23,59,59);

    // Future
    LocalDateTime timeFuture = LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth() ,tomorrow.getDayOfMonth(),20,55,00);

    commandService.addCommand(customCommandSchema(store.getId(), time1));
    commandService.addCommand(customCommandSchema(store.getId(), time2));
    commandService.addCommand(customCommandSchema(store.getId(), time3));
    commandService.addCommand(customCommandSchema(store.getId(), time4));
    commandService.addCommand(customCommandSchema(store.getId(), time5));
    commandService.addCommand(customCommandSchema(store.getId(), time6));
    commandService.addCommand(customCommandSchema(store.getId(), time7));
    commandService.addCommand(customCommandSchema(store.getId(), time8));
    commandService.addCommand(customCommandSchema(store.getId(), time9));
    commandService.addCommand(customCommandSchema(store.getId(), timeOld));
    commandService.addCommand(customCommandSchema(store.getId(), timeFuture));

    // Date de la commande
    LocalDate commandDate = LocalDate.from(now);

    // Date de consultation des Slot
    LocalDateTime consultationDate = commandDate.atTime(11, 9,50);

    //Temps de prépa commande
    int preparationTime = 20;

    // Slot non disponible pour la commande
    int slotMissingOpeningMorning = 1;
    int slotMissingOpeningAfternoon = 1;
    Duration durationMissing = Duration.between(consultationDate, consultationDate.plus(Duration.ofMinutes(preparationTime)));
    Long slotMissing = durationMissing.toMinutes()/ store.getFrequenceSlotTime() + slotMissingOpeningMorning;

    ////////////////////////

    List<LocalDateTime> findAllSlotAvailable = commandService.findAllSlotAvailable(
            new FindListOfSlotTimeAvailableDto(
                    commandDate,
                    store.getId(),
                    preparationTime,
                    consultationDate)
    );

   long storeSlotesAvailibility = storeSchedules
            .stream()
            .map(storeSchedule-> Duration.between(storeSchedule.getOpeningTime(), storeSchedule.getClosingTime()))
            .mapToLong(duration-> duration.toMinutes() / store.getFrequenceSlotTime())
            .sum()
           - mornigCommand
           - slotMissing;


    Assertions.assertEquals(storeSlotesAvailibility, findAllSlotAvailable.size());

  }

  /**
   * Recherche des slots diponible pour une commande avant ouvertue du commerce
   */
  @Test
  void should_find_list_of_slot_available_before_opening_time() {
    // Creation store - produit - commande
    ProInformationDto pro = createPro();

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    // Date de consultation des Slot
    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);

    //Temps de prépa commande
    int preparationTime = 5;

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest1 = findSlotAvail(
            storeSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime
    );

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest2 = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
   int preparationTime = 5;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest1 = findSlotAvail(
            storeSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime1
    );

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest2 = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    //Temps de prépa commande
    int preparationTime = 5;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    // Date de consultation des Slot
    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest1 = findSlotAvail(
            storeSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime
    );

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest2 = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest1 = findSlotAvail(
            storeSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime1
    );

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest2 = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    // Date de consultation des Slot
    LocalDateTime consultationDateBeforeOpeningTime = commandDate.atTime(8, 9,50);
    LocalDateTime consultationDateBeforeOpeningTime2 = commandDate.atTime(16, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Génération de commande
    createCommands(commandDate, 5, store);

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest1 = findSlotAvail(
            storeSchedules,
            store,
            preparationTime,
            consultationDateBeforeOpeningTime
    );

    // Calcul du nombre de slot disponible avant ouverture du restaurant
    List<LocalDateTime> findSlotAvailableTest2 = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    LocalDateTime consultationDateDuringOpeningTime1 = commandDate.atTime(12, 9,50);
    LocalDateTime consultationDateDuringOpeningTime2 = commandDate.atTime(19, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest1 = findSlotAvail(
            storeSchedules,
            store,
            preparationTime,
            consultationDateDuringOpeningTime1
    );

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest2 = findSlotAvail(
            storeSchedules,
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

    // Horaire du commerce
    List<StoreSchedule> storeSchedules = List.of(
            new StoreSchedule(
                    LocalTime.of(11, 00),
                    LocalTime.of(14,00)
            ),
            new StoreSchedule(
                    LocalTime.of(18, 00),
                    LocalTime.of(22,00)
            )
    );

    StoreEntity store = createStore(pro, storeSchedules);

    LocalDateTime today = LocalDateTime.now().plusDays(1);

    // Date de la commande
    LocalDate commandDate = LocalDate.from(today);

    LocalDateTime consultationDateDuringOpeningTime = commandDate.atTime(23, 9,50);

    //Temps de prépa commande
    int preparationTime = 15;

    // Calcul du nombre de slot disponible
    List<LocalDateTime> findSlotAvailableTest = findSlotAvail(
            storeSchedules,
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
    StoreEntity createdStore = createStore(createdPro, null);
    StoreEntity createdStore2;

    store = isStoreExist ?
            createdStore :
            StoreEntityBuilder.aStoreEntity().withId(BigInteger.valueOf(0)).build();

    // Création produits
    List<AddProductResponseDto> createProductList = createProduct(createdStore.getId());

    // Creation commande
    createProductsInCommand(createProductList);

    // Creation Pro2 + Store2 + produits2 et ajout d'un produit à la commande 1
    if(!isProdutcBelongToStore) {
      createdPro2 = createPro();
      createdStore2 = createStore(createdPro2, null);
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

  /**
   * Creation Store
   * @param createdPro - ProInformationDto - Données sur le professionnel
   * @return StoreEntity
   */
  public StoreEntity createStore(ProInformationDto createdPro, List<StoreSchedule> storeSchedules) {

    if(storeSchedules == null || storeSchedules.isEmpty()) storeSchedules = List.of(
            new StoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00)),
            new StoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,00,00))
    );

    AddStoreDto addStoreSchema = new AddStoreDto(
            createdPro.id(),
            "magasin",
            "rue des carriere",
            "auterive",
            "31190",
            storeSchedules,
            10);
    StoreEntity createdStore = storeService.createStore(addStoreSchema);
    return createdStore;
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

  /**
   * Calcul du nombre de slot disponible pour une commande
   * @param storeSchedules
   * @param store
   * @param preparationTime
   * @param consultationDate
   * @return long
   */
  public List<LocalDateTime> findSlotAvail(
          List<StoreSchedule> storeSchedules,
          StoreEntity store,
          int preparationTime,
          LocalDateTime consultationDate
  ) {
    // Heure d'observation avant début d'ouverture du matin ou du soir
    boolean isObservationBeforeOpening = storeSchedules
            .stream()
            .allMatch(storeSchedule ->storeSchedule.getOpeningTime().isAfter(consultationDate.toLocalTime()));

    // Liste des slot disponible
    List<LocalDateTime> slotsAvailibilityList = getSlotsAvailibilityInOneDay(store.getFrequenceSlotTime(), consultationDate.toLocalDate());

    // Slot manquant suite temps de préparation
    slotsAvailibilityList = filterSlotByStoreSchedule(
            storeSchedules,
            preparationTime,
            consultationDate,
            slotsAvailibilityList
            );

    // slot manquant suite à l'horaire d'observation
    slotsAvailibilityList = filterSlotByConsultationDate(
            preparationTime,
            consultationDate,
            slotsAvailibilityList);

    // Filtre les slots déja occupé
    slotsAvailibilityList = filterBusySlot(
            consultationDate,
            LocalDateTime.of(
              consultationDate.getYear(),
              consultationDate.getMonth(),
              consultationDate.getDayOfMonth(),
              23,
              59,
              59
            ),
            store,
            slotsAvailibilityList);



    return slotsAvailibilityList;
  }

  private List<LocalDateTime> getSlotsAvailibilityInOneDay(Integer frequenceSlotTime, LocalDate commandDate) {
    int slotInOneDay = (int) Math.floor(24 * 60 / (float) frequenceSlotTime);
    LocalDateTime begin = LocalDateTime.of(
    commandDate.getYear(),
    commandDate.getMonth(),
    commandDate.getDayOfMonth(),
    0,
    0,
    0);

    LocalDateTime end = LocalDateTime.of(
            commandDate.getYear(),
            commandDate.getMonth(),
            commandDate.getDayOfMonth(),
            23,
            59,
            0);
    return Stream
            .iterate(begin, action -> action.isBefore(end), action -> action.plusMinutes(frequenceSlotTime))
            .collect(Collectors.toList());
  }

  /**
   * Nombre de slot manquant du au temps de préparation
   * @param storeSchedules
   * @param preparationTime
   * @param consultationDate
   * @return List<LocalDateTime>
   */
  public List<LocalDateTime> filterSlotByStoreSchedule(
          List<StoreSchedule> storeSchedules,
          int preparationTime,
          LocalDateTime consultationDate,
          List<LocalDateTime> slotAvailibilityList) {

   List<LocalDateTime> filterSlotAvailibility = slotAvailibilityList.stream().filter(
            slot->{
              return storeSchedules
                  .stream()
                  .anyMatch(schedule-> CommonFunction.isSlotInStoreSchedule(slot, consultationDate, schedule, preparationTime));
              })
            .collect(Collectors.toList());

    return filterSlotAvailibility;
  }

  /**
   * Nombre de Slot manquant due à l'heure d'observation
   * @param consultationDate
   * @param  preparationTime
   * @return List<LocalDateTime>
   */
  public List<LocalDateTime> filterSlotByConsultationDate(
          int preparationTime,
          LocalDateTime consultationDate,
          List<LocalDateTime> slotAvailibilityList) {

    List<LocalDateTime> filterSlotAvailibility = slotAvailibilityList
            .stream()
            .filter(
            slot->slot.isAfter(consultationDate.plusMinutes(preparationTime))
    ).collect(Collectors.toList());

    return  filterSlotAvailibility;
  }

  /**
   * Nombre de slot occupés par une commande
   * @return
   */
  public List<LocalDateTime> filterBusySlot(LocalDateTime beginDay, LocalDateTime endDay, StoreEntity store, List<LocalDateTime> slotAvailibilityList) {

    List<LocalDateTime> commandsSlotTime = commandRepository
            .findCommandsByStoreIdDate(beginDay, endDay, store.getId())
            .stream()
            .map(CommandEntity::getSlotTime)
            .collect(Collectors.toList());
    List<LocalDateTime> filterSlotAvailibility = slotAvailibilityList
            .stream()
            .filter(slot->!commandsSlotTime.contains(slot))
            .collect(Collectors.toList());
    return filterSlotAvailibility;
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
