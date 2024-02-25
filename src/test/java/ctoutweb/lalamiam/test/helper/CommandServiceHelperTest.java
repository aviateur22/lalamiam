//package ctoutweb.lalamiam.test.helper;
//
//import ctoutweb.lalamiam.helper.CommandServiceHelper;
//import ctoutweb.lalamiam.model.DailyStoreSchedule;
//import ctoutweb.lalamiam.model.WeeklyStoreSchedule;
//import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
//import ctoutweb.lalamiam.model.dto.ProInformationDto;
//import ctoutweb.lalamiam.model.dto.AddProductDto;
//import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
//import ctoutweb.lalamiam.model.dto.AddStoreDto;
//import ctoutweb.lalamiam.model.ProductWithQuantity;
//import ctoutweb.lalamiam.repository.ProRepository;
//import ctoutweb.lalamiam.repository.ProductRepository;
//import ctoutweb.lalamiam.repository.entity.StoreEntity;
//import ctoutweb.lalamiam.service.ProService;
//import ctoutweb.lalamiam.service.ProductService;
//import ctoutweb.lalamiam.service.StoreService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigInteger;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class CommandServiceHelperTest {
//
//  @Autowired
//  ProService proService;
//
//  @Autowired
//  CommandServiceHelper commandServiceHelper;
//
//  @Autowired
//  CommandServiceHelper storeServiceHelper;
//  @Autowired
//  ProRepository proRepository;
//
//  @Autowired
//  ProductService productService;
//
//  @Autowired
//  ProductRepository productRepository;
//
//  @Autowired
//  StoreService storeService;
//
//  @BeforeEach
//  void beforeEach() {
//    proRepository.truncateAll();
//  }
//
//  @Test
//  void should_find_list_of_product_by_id() {
//    // Creation Pro
//    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));
//
//    // Jours
//    List<Integer> days = List.of(2, 3, 4, 5);
//    List<Integer> dayMonday = List.of(1);
//    List<Integer> daySunday = List.of(7);
//
//    // Horaires
//    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
//    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));
//
//    // Horaires du commerce
//    List<WeeklyStoreSchedule> storeWeekDaySchedules = List.of(
//            new WeeklyStoreSchedule(days, storeScheduleMorning),
//            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
//            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
//            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
//    );
//
//    // Creation Store
//    AddStoreDto addStoreSchema = new AddStoreDto(
//            createdPro.id(),
//            "magasin",
//            "rue des carriere",
//            "auterive",
//            "31190",
//            storeWeekDaySchedules,
//            10);
//    StoreEntity store= storeService.createStore(addStoreSchema);
//
//    // Ajout produits
//    AddProductDto addProductSchema1 = new AddProductDto("pomme",
//            10D,
//            "initial description",
//            5,
//            "s",
//            store.getId());
//    AddProductDto addProductSchema2 = new AddProductDto("poire",
//            20D,
//            "initial description",
//            5,
//            "s",
//            store.getId());
//    AddProductDto addProductSchema3 = new AddProductDto("bannane",
//            10D,
//            "initial description",
//            5,
//            "s",
//            store.getId());
//
//    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
//    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
//    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);
//
//    List<BigInteger> productsId = new ArrayList<>();
//    productsId.add(addProduct1.id());
//    productsId.add(addProduct2.id());
//    productsId.add(addProduct3.id());
//
//    Assertions.assertEquals(3, productRepository.findByStore(store).size());
//  }
//  @Test
//  void should_calculate_command_price() throws Exception {
//    // Creation Pro
//    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));
//
//    // Jours
//    List<Integer> days = List.of(2, 3, 4, 5);
//    List<Integer> dayMonday = List.of(1);
//    List<Integer> daySunday = List.of(7);
//
//    // Horaires
//    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
//    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));
//
//    // Horaires du commerce
//    List<WeeklyStoreSchedule> storeWeekDaySchedules = List.of(
//            new WeeklyStoreSchedule(days, storeScheduleMorning),
//            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
//            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
//            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
//    );
//
//
//    // Creation Store
//    AddStoreDto addStoreSchema = new AddStoreDto(
//            createdPro.id(),
//            "magasin",
//            "rue des carriere",
//            "auterive",
//            "31190",
//            storeWeekDaySchedules,
//            10);
//    StoreEntity store= storeService.createStore(addStoreSchema);
//
//    // Ajout produits
//    AddProductDto addProductSchema1 = new AddProductDto("pomme",
//            1.5,
//            "initial description",
//            2,
//            "s",
//            store.getId());
//    AddProductDto addProductSchema2 = new AddProductDto("poire",
//            2.35,
//            "initial description",
//            5,
//            "s",
//            store.getId());
//    AddProductDto addProductSchema3 = new AddProductDto("bannane",
//            10D,
//            "initial description",
//            10,
//            "s",
//            store.getId());
//
//    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
//    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
//    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);
//
//    List<ProductWithQuantity> products = new ArrayList<>();
//    products.add(new ProductWithQuantity(addProduct1.id(), 1));
//    products.add(new ProductWithQuantity(addProduct2.id(), 1));
//    products.add(new ProductWithQuantity(addProduct3.id(), 1));
//
//    // Assertions.assertEquals(13.85, storeServiceHelper.calculateCommandPrice(products));
//  }
//
//  @Test
//  void should_calculate_command_preparation_time() {
//    // Creation Pro
//    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));
//
//    // Jours
//    List<Integer> days = List.of(2, 3, 4, 5);
//    List<Integer> dayMonday = List.of(1);
//    List<Integer> daySunday = List.of(7);
//
//    // Horaires
//    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
//    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));
//
//    // Horaires du commerce
//    List<WeeklyStoreSchedule> storeWeekDaySchedules = List.of(
//            new WeeklyStoreSchedule(days, storeScheduleMorning),
//            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
//            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
//            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
//    );
//
//    // Creation Store
//    AddStoreDto addStoreSchema = new AddStoreDto(
//            createdPro.id(),
//            "magasin",
//            "rue des carriere",
//            "auterive",
//            "31190",
//            storeWeekDaySchedules,
//            10);
//    StoreEntity store= storeService.createStore(addStoreSchema);
//
//    // Ajout produits
//    AddProductDto addProductSchema1 = new AddProductDto("pomme",
//            1.5,
//            "initial description",
//            2,
//            "s",
//            store.getId());
//    AddProductDto addProductSchema2 = new AddProductDto("poire",
//            2.35,
//            "initial description",
//            5,
//            "s",
//            store.getId());
//    AddProductDto addProductSchema3 = new AddProductDto("bannane",
//            10D,
//            "initial description",
//            10,
//            "s",
//            store.getId());
//
//    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
//    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
//    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);
//
//    List<ProductWithQuantity> products = new ArrayList<>();
//    products.add(new ProductWithQuantity(addProduct1.id(), 1));
//    products.add(new ProductWithQuantity(addProduct2.id(), 1));
//    products.add(new ProductWithQuantity(addProduct3.id(), 2));
//
//    //Assertions.assertEquals(27, commandServiceHelper.calculateCommandPreparationTime(products));
//
//  }
//}
