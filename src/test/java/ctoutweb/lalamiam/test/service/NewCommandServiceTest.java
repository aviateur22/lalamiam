package ctoutweb.lalamiam.test.service;

import ctoutweb.lalamiam.exception.CommandException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.helper.NewSlotHelper;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.ProductService;
import ctoutweb.lalamiam.service.StoreService;
import ctoutweb.lalamiam.service.serviceImpl.NewCommandServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


public class NewCommandServiceTest {
  NewCommandServiceImp commandService;
  @Test
  void updateCommand_with_valid_information() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    LocalDateTime slotTime = LocalDateTime.now();

    // Mock
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(storeId, commandId, slotTime));

    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    // Spy de commandService
    NewCommandServiceImp commandServiceImpSpy = spy(new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper));

    doReturn(getFakeStoreProductsInformation()).when(commandServiceImpSpy).getStoreProductsForCommand(any(Long.class),any(CommandEntity.class));

    //
    StoreProductsInformationDto storeProductInformation = commandServiceImpSpy.getStoreProductToUpdateCommand(storeId, commandId);

    Assertions.assertNotNull(storeProductInformation);
    Assertions.assertEquals("0623225100", storeProductInformation.clientPhone());
    Assertions.assertEquals(fakeProductWithQuantityForStore(), storeProductInformation.storeProducts());
    verify(commandServiceImpSpy, times(1)).getStoreProductsForCommand(any(Long.class),any(CommandEntity.class));
  }
  @Test
  void updateCommand_when_command_is_null() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    LocalDateTime slotTime = LocalDateTime.now();


    // Mock
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(null);

    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    // Spy de commandService
    NewCommandServiceImp commandServiceImpSpy = spy(new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper));

    doReturn(getFakeStoreProductsInformation()).when(commandServiceImpSpy).getStoreProductsForCommand(any(Long.class),any(CommandEntity.class));

    Exception exception = Assertions.assertThrows(CommandException.class, ()-> commandServiceImpSpy.getStoreProductToUpdateCommand(storeId, commandId));
    Assertions.assertEquals("La commande à mettre à jour n'existe pas", exception.getMessage());
  }
  @Test
  void updateCommand_when_command_not_belong_to_store() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    LocalDateTime slotTime = LocalDateTime.now();

    // Mock
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(Long.valueOf(3), commandId, slotTime));

    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    // Spy de commandService
    NewCommandServiceImp commandServiceImpSpy = spy(new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper));

    doReturn(getFakeStoreProductsInformation()).when(commandServiceImpSpy).getStoreProductsForCommand(any(Long.class),any(CommandEntity.class));

    //
    Exception exception = Assertions.assertThrows(CommandException.class, ()-> commandServiceImpSpy.getStoreProductToUpdateCommand(storeId, commandId));
    Assertions.assertEquals("Cette commande n'est pas rattaché au commerce", exception.getMessage());
  }
  @Test
  void getStoreProductsForCommand_with_command_and_store_product() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    LocalDateTime slotTimeRequest = LocalDateTime.now();

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);

    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper, productService);

    commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    /**
     * When
     */
    // Fake CommandEntity
    CommandEntity command = getFakeCommandEntity(storeId, commandId, slotTimeRequest);
    when(commandTransactionSession.getCommand(any(Long.class))).thenReturn(command);

    // Fake List<ProductEntity>
    when(productService.getStoreProducts(any(Long.class))).thenReturn(fakeStoreProductEntiy());

    StoreProductsInformationDto commandInformation = commandService.getStoreProductsForCommand(storeId, command);

    /**
     * Then
     */
    Assertions.assertEquals("0623274100", commandInformation.clientPhone());
    Assertions.assertEquals(fakeStoreProductEntiy().size(), commandInformation.storeProducts().size());
    Assertions.assertEquals(1, getProductWithQuantityById(Long.valueOf(1), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(2, getProductWithQuantityById(Long.valueOf(2), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(3, getProductWithQuantityById(Long.valueOf(3), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(4, getProductWithQuantityById(Long.valueOf(4), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(5), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(6), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(7), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(8), commandInformation.storeProducts()).selectQuantity());
  }
  @Test
  void getStoreProductsForCommand_without_command() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper, productService);

    commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    /**
     * When
     */
    // Fake CommandEntity
    CommandEntity command = null;
    when(commandTransactionSession.getCommand(any(Long.class))).thenReturn(command);

    // Fake List<ProductEntity>
    when(productService.getStoreProducts(any(Long.class))).thenReturn(fakeStoreProductEntiy());

    StoreProductsInformationDto commandInformation = commandService.getStoreProductsForCommand(storeId, command);

    /**
     * Then
     */
    Assertions.assertEquals(null, commandInformation.clientPhone());
    Assertions.assertEquals(fakeStoreProductEntiy().size(), commandInformation.storeProducts().size());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(1), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(2), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(3), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(4), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(5), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(6), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(7), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(Long.valueOf(8), commandInformation.storeProducts()).selectQuantity());
  }
  @Test
  void getStoreProductsForCommand_with_no_product_in_store() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    LocalDateTime slotTimeRequest = LocalDateTime.now();

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);

    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper, productService);

    commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    /**
     * When
     */
    // Fake CommandEntity
    CommandEntity command = getFakeCommandEntity(storeId, commandId, slotTimeRequest);
    when(commandTransactionSession.getCommand(any(Long.class))).thenReturn(command);

    // Fake List<ProductEntity>
    when(productService.getStoreProducts(any(Long.class))).thenReturn(Arrays.asList());

    /**
     * Then
     */

    StoreProductsInformationDto storeProductsInformation = commandService.getStoreProductsForCommand(storeId, command);

    Assertions.assertEquals(0, storeProductsInformation.storeProducts().size());
  }
  @Test
  void getCommand_with_valid_command_and_store() {
    /**
     * Given
     */
    Long commandId = Long.valueOf(1);
    Long storeId = Long.valueOf(1);
    LocalDateTime slotTimeRequest = LocalDateTime.now();

    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(any(Long.class))).thenReturn(getFakeCommandEntity(storeId, commandId, slotTimeRequest));

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper, productService);

    commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService, slotHelper
    );

    /**
     * When
     */
    RegisterCommandDto registerCommand = commandService.getCommand(storeId, commandId);

    /**
     * then
     */
    // Phone
    String phoneClient = getFakeCommandEntity(storeId, commandId, slotTimeRequest).getClientPhone();

    // SlotTime
    LocalDateTime slotTime = getFakeCommandEntity(storeId, commandId, slotTimeRequest).getSlotTime();

    // Produits de la commande
    List<ProductWithQuantityDto> productsWithQuantityDto = getFakeCommandEntity(storeId, commandId, slotTimeRequest).getCommandProducts()
            .stream()
            .map(productQuantityMapper)
            .toList();

    // Temps de preparation
    Integer preparationTime = getFakeCommandEntity(storeId, commandId, slotTimeRequest).getPreparationTime();

    // Prix
    Double price = getFakeCommandEntity(storeId, commandId, slotTimeRequest).getCommandPrice();

    // Nombre de produit
    Integer productQuantity = getFakeCommandEntity(storeId, commandId, slotTimeRequest).getProductQuantity();

    Assertions.assertEquals(commandId, registerCommand.getCommandId());
    Assertions.assertEquals(storeId, registerCommand.getStoreId());
    Assertions.assertEquals(slotTime, registerCommand.getManualCommandInformation().getSlotTime());
    Assertions.assertEquals(phoneClient, registerCommand.getManualCommandInformation().getPhoneClient());
    Assertions.assertEquals(productsWithQuantityDto, registerCommand.getManualCommandInformation().getSelectProducts());
    Assertions.assertEquals(preparationTime, registerCommand.getCalculatedCommandInformation().getCommandPreparationTime());
    Assertions.assertEquals(price, registerCommand.getCalculatedCommandInformation().getCommandePrice());
    Assertions.assertEquals(productQuantity, registerCommand.getCalculatedCommandInformation().getProductQuantity());
  }
  @Test
  void getCommand_without_command() {
    /**
     * Given
     */
    Long commandId = Long.valueOf(1);
    Long storeId = Long.valueOf(1);

    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(any(Long.class))).thenReturn(null);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper, productService);

    commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    /**
     * When
     */
    Exception exception = Assertions.assertThrows(RuntimeException.class, ()-> commandService.getCommand(storeId, commandId));
    Assertions.assertEquals("Cette commande n'existe pas".toLowerCase(), exception.getMessage().toLowerCase());
    Assertions.assertThrows(RuntimeException.class, ()-> commandService.getCommand(storeId, commandId));


  }
  @Test
  void validateProductsSelection_() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "06402101";
    List<ProductWithQuantity> productsSelect = getFakeProductWithQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    commandService.validateProductsSelection(storeId, commandId, productSelectInformation);


  }
  @Test
  void validateProductsSelection_without_command_id() {
    Long storeId = Long.valueOf(1);
    Long commandId = null;
    String clientPhone = "0623471";
    List<ProductWithQuantity> productsSelect = getFakeProductWithQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    commandService.validateProductsSelection(storeId, commandId, productSelectInformation);

  }
  @Test
  void validateProductsSelection_with_command_not_belong_to_store() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "0623471";
    List<ProductWithQuantity> productsSelect = getFakeProductWithQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            Long.valueOf(2),
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(
            storeId,
            commandId,
            productSelectInformation)
    );
    Assertions.assertEquals("Cette commande n'est pas rattaché au commerce", exception.getMessage());

  }
  @Test
  void validateProductsSelection_without_phone() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "";
    List<ProductWithQuantity> productsSelect = getFakeProductWithQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));
    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(storeId, commandId, productSelectInformation));
    Assertions.assertEquals("Le numéro de téléphone est obligatoire", exception.getMessage());

  }
  @Test
  void validateProductsSelection_with_empty_command() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "062357";
    List<ProductWithQuantity> productsSelect = Arrays.asList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(storeId, commandId, productSelectInformation));
    Assertions.assertEquals("La commande ne peut pas être vide", exception.getMessage());

  }
  @Test
  void validateProductsSelection_with_null_command() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "062357";
    List<ProductWithQuantity> productsSelect = null;

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(storeId, commandId, productSelectInformation));
    Assertions.assertEquals("La commande ne peut pas être vide", exception.getMessage());

  }
  @Test
  void validateProductsSelection_with_product_not_belong_to_store() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "062357";
    List<ProductWithQuantity> productsSelect = getFakeProductWithQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService ProdutcId1
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(Long.valueOf(1))).thenReturn(getFakeProductNotBelongToStore(Long.valueOf(1)));

    when(productService.findProduct(Long.valueOf(2))).thenReturn(getFakeProductNotBelongToStore(Long.valueOf(2)));

    when(productService.findProduct(Long.valueOf(3))).thenReturn(getFakeProductNotBelongToStore(Long.valueOf(3)));

    when(productService.findProduct(Long.valueOf(4))).thenReturn(getFakeProductNotBelongToStore(Long.valueOf(4)));


    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(storeId, commandId, productSelectInformation));
    Assertions.assertEquals("Le produit coco n'est pas référencé dans ce commerce", exception.getMessage());

  }
  @Test
  void validateProductsSelection_with_product_not_available() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "062357";
    List<ProductWithQuantity> productsSelect = getFakeProductWithQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService ProdutcId1
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(Long.valueOf(1))).thenReturn(getFakeProductNotAvailable(Long.valueOf(1)));

    when(productService.findProduct(Long.valueOf(2))).thenReturn(getFakeProductNotAvailable(Long.valueOf(2)));

    when(productService.findProduct(Long.valueOf(3))).thenReturn(getFakeProductNotAvailable(Long.valueOf(3)));

    when(productService.findProduct(Long.valueOf(4))).thenReturn(getFakeProductNotAvailable(Long.valueOf(4)));


    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(storeId, commandId, productSelectInformation));
    Assertions.assertEquals("Le produit coco n'est plus disponible", exception.getMessage());

  }
  @Test
  void validateProductsSelection_without_product_quantity() {
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "062357";
    List<ProductWithQuantity> productsSelect = getFakeProductWithoutQuantityList();

    ProductSelectInformationDto productSelectInformation = new ProductSelectInformationDto(
            productsSelect,
            clientPhone
    );

    // Mock NewCommandServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);

    // Mock CommandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.getCommand(commandId)).thenReturn(getFakeCommandEntity(
            storeId,
            commandId,
            LocalDateTime.now()));

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService ProdutcId1
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(Long.valueOf(1))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    // Mock NewSlotHelper
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandService.validateProductsSelection(storeId, commandId, productSelectInformation));
    Assertions.assertEquals("Merci d'indiquer la quantité pour le produit coco", exception.getMessage());

  }
  @Test
  void getStoreSlotAvailibility_without_commands_in_store() {

    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate commandDate = getNextRequestDate(weekDay.getId());
    LocalDateTime consulationDate = commandDate.atTime(8,30,0);

    // Données pour rechercher les SLOTs
    CommandInformationDto storeSlotInformation = new CommandInformationDto(
            storeId,
            commandId,
            commandDate,
            consulationDate,
            getFakeProductWithQuantityList(),
            null
    );

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);
    when(storeService.findStoreById(any(Long.class))).thenReturn(getFakeStore(storeId));
    when(storeService.findStoreSchedulesByDay(any(StoreEntity.class), any(WeekDayEntity.class)))
            .thenReturn(getFakeDoubleStoreSchedule(weekDay, getFakeStore(storeId)));
    when(storeService.findStoreSlotsWithoutConstraintByDay(any(LocalDateTime.class), any(Integer.class)))
            .thenReturn(getFakeStoreSlotsWithoutConstraintByDay(getFakeStore(storeId).getFrequenceSlotTime(), commandDate.atStartOfDay()));

    // Mock NewStoreServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    when(commandServiceHelper.calculateCommandPreparationTime(anyList())).thenReturn(10);

    // Mock commandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);
    when(commandRepository.findCommandsByStoreIdDate(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            any(Long.class))
    ).thenReturn(Arrays.asList());

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    // SlotHelper
    NewSlotHelper slotHelper = new NewSlotHelper(storeService);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    List<LocalDateTime> slotAvailibility = commandService.getStoreSlotAvailibility(storeSlotInformation);

    Assertions.assertEquals(40, slotAvailibility.size());
  }
  @Test
  void getStoreSlotAvailibility_with_commands_in_store() {

    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDate commandDate = getNextRequestDate(weekDay.getId());
    LocalDateTime consulationDate = commandDate.atTime(15,30,0);
    LocalDateTime selectSlotTime = commandDate.atTime(17,30,0);

    // Données pour rechercher les SLOTs
    CommandInformationDto storeSlotInformation = new CommandInformationDto(
            storeId,
            commandId,
            commandDate,
            consulationDate,
            getFakeProductWithQuantityList(),
            selectSlotTime
    );

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);
    when(storeService.findStoreById(any(Long.class))).thenReturn(getFakeStore(storeId));
    when(storeService.findStoreSchedulesByDay(any(StoreEntity.class), any(WeekDayEntity.class)))
            .thenReturn(getFakeDoubleStoreSchedule(weekDay, getFakeStore(storeId)));
    when(storeService.findStoreSlotsWithoutConstraintByDay(any(LocalDateTime.class), any(Integer.class)))
            .thenReturn(getFakeStoreSlotsWithoutConstraintByDay(getFakeStore(storeId).getFrequenceSlotTime(), commandDate.atStartOfDay()));

    // Mock NewStoreServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    when(commandServiceHelper.calculateCommandPreparationTime(anyList())).thenReturn(10);

    // Mock commandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);

    // Mock CommandRepository
    CommandRepository commandRepository = mock(CommandRepository.class);
    when(commandRepository.findCommandsByStoreIdDate(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            any(Long.class))
    ).thenReturn(getFakeCommands(commandDate));

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    // SlotHelper
    NewSlotHelper slotHelper = new NewSlotHelper(storeService);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    List<LocalDateTime> slotAvailibilityList = commandService.getStoreSlotAvailibility(storeSlotInformation);

    Assertions.assertEquals(20, slotAvailibilityList.size());

    // Vérification que les crenaux occupé ne soit pas présent dans la liste des créneaux disponible
    Assertions.assertEquals(0, slotAvailibilityList
            .stream()
            .filter(slot->getFakeCommands(commandDate).contains(slot))
            .toList()
            .size());
  }
  @Test
  void getStoreSlotAvailibility_next_day_without_command_in_store() {

    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDateTime consulationDate = getNextRequestDate(weekDay.getId()).atTime(15,0);
    LocalDate commandDate = consulationDate.plusDays(1).toLocalDate();

    // Données pour rechercher les SLOTs
    CommandInformationDto storeSlotInformation = new CommandInformationDto(
            storeId,
            commandId,
            commandDate,
            consulationDate,
            getFakeProductWithQuantityList(),
            null
    );

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);
    when(storeService.findStoreById(any(Long.class))).thenReturn(getFakeStore(storeId));
    when(storeService.findStoreSchedulesByDay(any(StoreEntity.class), any(WeekDayEntity.class)))
            .thenReturn(getFakeDoubleStoreSchedule(weekDay, getFakeStore(storeId)));
    when(storeService.findStoreSlotsWithoutConstraintByDay(any(LocalDateTime.class), any(Integer.class)))
            .thenReturn(getFakeStoreSlotsWithoutConstraintByDay(getFakeStore(storeId).getFrequenceSlotTime(), commandDate.atStartOfDay()));

    // Mock NewStoreServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    when(commandServiceHelper.calculateCommandPreparationTime(anyList())).thenReturn(10);

    // Mock commandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);

    // Mock CommandRepository   List<CommandEntity>
    CommandRepository commandRepository = mock(CommandRepository.class);
    when(commandRepository.findCommandsByStoreIdDate(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            any(Long.class))
    ).thenReturn(Arrays.asList());

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    // SlotHelper
    NewSlotHelper slotHelper = new NewSlotHelper(storeService);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    List<LocalDateTime> slotAvailibilityList = commandService.getStoreSlotAvailibility(storeSlotInformation);

    Assertions.assertEquals(40, slotAvailibilityList.size());
  }
  @Test
  void getStoreSlotAvailibility_next_day_with_command_in_store() {

    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    WeekDayEntity weekDay = new WeekDayEntity(1);
    LocalDateTime consulationDate = getNextRequestDate(weekDay.getId()).atTime(15,0);
    LocalDate commandDate = consulationDate.plusDays(1).toLocalDate();
    LocalDateTime selectSlotTime = commandDate.atTime(17,30,0);

    // Données pour rechercher les SLOTs
    CommandInformationDto storeSlotInformation = new CommandInformationDto(
            storeId,
            commandId,
            commandDate,
            consulationDate,
            getFakeProductWithQuantityList(),
            selectSlotTime
    );

    // Mock StoreService
    StoreService storeService = mock(StoreService.class);
    when(storeService.findStoreById(any(Long.class))).thenReturn(getFakeStore(storeId));
    when(storeService.findStoreSchedulesByDay(any(StoreEntity.class), any(WeekDayEntity.class)))
            .thenReturn(getFakeDoubleStoreSchedule(weekDay, getFakeStore(storeId)));
    when(storeService.findStoreSlotsWithoutConstraintByDay(any(LocalDateTime.class), any(Integer.class)))
            .thenReturn(getFakeStoreSlotsWithoutConstraintByDay(getFakeStore(storeId).getFrequenceSlotTime(), commandDate.atStartOfDay()));

    // Mock NewStoreServiceHelper
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    when(commandServiceHelper.calculateCommandPreparationTime(anyList())).thenReturn(10);

    // Mock commandTransactionSession
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);

    // Mock CommandRepository   List<CommandEntity>
    CommandRepository commandRepository = mock(CommandRepository.class);
    when(commandRepository.findCommandsByStoreIdDate(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            any(Long.class))
    ).thenReturn(getFakeCommands(commandDate));

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    // SlotHelper
    NewSlotHelper slotHelper = new NewSlotHelper(storeService);

    NewCommandServiceImp commandService = new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    );

    List<LocalDateTime> slotAvailibilityList = commandService.getStoreSlotAvailibility(storeSlotInformation);

    Assertions.assertEquals(34, slotAvailibilityList.size());

    // Vérification que les crenaux occupé ne soit pas présent dans la liste des créneaux disponible
    Assertions.assertEquals(0, slotAvailibilityList
            .stream()
            .filter(slot->getFakeCommands(commandDate).contains(slot))
            .toList()
            .size());
  }
  @Test
  void validateSlot_with_slot_avail() {
    LocalDateTime selectSlot = LocalDate.now().atTime(10, 30);
    CommandInformationDto commandInformation = new CommandInformationDto(
            Long.valueOf(1),
            Long.valueOf(1),
            LocalDate.now(),
            LocalDateTime.now(),
            Arrays.asList(),
            LocalDateTime.now().plusMinutes(55)
    );

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    // Spy de commandService
    NewCommandServiceImp commandServiceImpSpy = spy(new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper));
    doReturn(getFakeSlotAvailibility(commandInformation.commandDate())).when(commandServiceImpSpy).getStoreSlotAvailibility(commandInformation);

    commandServiceImpSpy.validateSlot(commandInformation);
    verify(commandServiceImpSpy, times(1)).getStoreSlotAvailibility(any(CommandInformationDto.class));

  }
  @Test
  void validateSlot_with_slot_not_avail() {
    LocalDateTime selectSlot = LocalDate.now().atTime(11, 55);
    CommandInformationDto commandInformation = new CommandInformationDto(
            Long.valueOf(1),
            Long.valueOf(1),
            LocalDate.now(),
            LocalDateTime.now(),
            Arrays.asList(),
            LocalDateTime.now().plusMinutes(55)

    );

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    CommandRepository commandRepository = mock(CommandRepository.class);
    ProductService productService = mock(ProductService.class);
    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    // Spy de commandService
    NewCommandServiceImp commandServiceImpSpy = spy(new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper));
    doReturn(getFakeSlotAvailibility(commandInformation.commandDate())).when(commandServiceImpSpy).getStoreSlotAvailibility(commandInformation);

    //commandServiceImpSpy.validateSlot(commandInformation, selectSlotTime );
    Exception exception = Assertions.assertThrows(CommandException.class, ()->commandServiceImpSpy.validateSlot(commandInformation));
    verify(commandServiceImpSpy, times(1)).getStoreSlotAvailibility(any(CommandInformationDto.class));
    Assertions.assertEquals("Le créneau demandé n'est plus disponible", exception.getMessage());
  }
  @Test
  void persistCommand() {

    Long storeId = Long.valueOf(1);
    Long commandId = null;
    Long prodId = Long.valueOf(1);

    ProPersitCommandDto persitCommandInformation = new ProPersitCommandDto(
            storeId,
            commandId,
            prodId,
            LocalDate.now(),
            LocalDate.now().atTime(8,50),
            Arrays.asList(
                    new ProductWithQuantity(Long.valueOf(1), 2),
                    new ProductWithQuantity(Long.valueOf(2), 2),
                    new ProductWithQuantity(Long.valueOf(3), 2)
            ),
            "0623275500",
            LocalDate.now().atTime(10,50)
    );

    // Mock CommandeTransactionCommand
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    when(commandTransactionSession.saveCommand(any(CommandInformationToSave.class))).thenReturn(getFakeCommandEntity(
            persitCommandInformation.storeId(),
            persitCommandInformation.commandId(),
            persitCommandInformation.selectSlotTime()
    ));
    when(commandTransactionSession.updateCommand(any(CommandInformationToUpdate.class))).thenReturn(getFakeCommandEntity(
            persitCommandInformation.storeId(),
            persitCommandInformation.commandId(),
            persitCommandInformation.selectSlotTime()
    ));

    NewCommandServiceHelper commandServiceHelper = mock(NewCommandServiceHelper.class);
    when(commandServiceHelper.findRegisterCommandInformation(any(CommandEntity.class))).thenReturn(
            getFakeRegisterCommand(
                    persitCommandInformation.storeId(),
                    getFakeCommandEntity(
                            persitCommandInformation.storeId(),
                            persitCommandInformation.commandId(),
                            persitCommandInformation.selectSlotTime()
                    )
            )
    );

    CommandRepository commandRepository = mock(CommandRepository.class);

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(any(Long.class))).thenReturn(getFakeProduct(storeId, Long.valueOf(1)));

    NewSlotHelper slotHelper = mock(NewSlotHelper.class);

    NewCommandServiceImp commandServiceImpSpy = spy(new NewCommandServiceImp(
            commandServiceHelper,
            commandTransactionSession,
            commandRepository,
            productService,
            slotHelper
    ));

    doNothing().when(commandServiceImpSpy).validateProductsSelection(
            any(Long.class),
            any(Long.class),
            any(ProductSelectInformationDto.class)
    );

    CommandInformationDto commandInformation = new CommandInformationDto(
            persitCommandInformation.storeId(),
            persitCommandInformation.commandId(),
            persitCommandInformation.commandDate(),
            persitCommandInformation.consultationDate(),
            persitCommandInformation.selectProducts(),
            persitCommandInformation.selectSlotTime());

    doReturn(getFakeSlotAvailibility(LocalDate.now())).when(commandServiceImpSpy).getStoreSlotAvailibility(any(CommandInformationDto.class));

    RegisterCommandDto registerCommand = commandServiceImpSpy.proPersistCommand(persitCommandInformation);
 //   verify(commandServiceImpSpy, times(1)).validateProductsSelection(any(Long.class), any(Long.class), any(ProductSelectInformationDto.class));

    Assertions.assertNotNull(registerCommand);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void test() {

  }
  /**
   * Fake d'une commande
   * @return List<CommandProductEntity>
   */
  private List<CommandProductEntity> fakeCommandProducList() {
    return Arrays.asList(
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(1),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(2, new ProductEntity(Long.valueOf(2),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(3, new ProductEntity(Long.valueOf(3),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(4, new ProductEntity(Long.valueOf(4),"test", 1D , "description", 5,"photo", true))
    );
  }

  /**
   * Fake productEntity
   * @param storeId Long
   * @param productId storeId
   * @return ProductEntity
   */
  private ProductEntity getFakeProduct(Long storeId, Long productId) {
    ProductEntity product = new ProductEntity(productId);
    product.setStore(new StoreEntity(storeId));
    product.setName("coco");
    product.setIsAvail(true);
    return product;

  }

  private StoreProductsInformationDto getFakeStoreProductsInformation() {
    return  new StoreProductsInformationDto(fakeProductWithQuantityForStore(), "0623225100");
  }

  /**
   * Liste des produits à afficher pour affichage coté client
   * @return List<ProductWithQuantityDto>
   */
  private List<ProductWithQuantityDto> fakeProductWithQuantityForStore() {
    return Arrays.asList(
            new ProductWithQuantityDto(Long.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(Long.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(Long.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(Long.valueOf(4), "test", "photo", 1D, 4, true),
            new ProductWithQuantityDto(Long.valueOf(5), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(Long.valueOf(6), "test", "photo", 1D, 4, true),
            new ProductWithQuantityDto(Long.valueOf(7), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(Long.valueOf(8), "test", "photo", 1D, 4, true),
            new ProductWithQuantityDto(Long.valueOf(9), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(Long.valueOf(10), "test", "photo", 1D, 4, true)
    );
  }

  /**
   * Fake prioduits d'un commerce
   * @return List<ProductEntity>
   */
  private List<ProductEntity> fakeStoreProductEntiy() {
    return Arrays.asList(
            new ProductEntity(Long.valueOf(1), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(2), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(3), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(4), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(5), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(6), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(7), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(8), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(9), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(10), "test", 15D,  "photo", 10, "photo", true)
    );
  }

  /**
   * Liste des produits à afficher pour affichage coté client - Uniquement 4 entrées   *
   * @return List<ProductWithQuantityDto>
   */
  private List<ProductWithQuantityDto> fakeProductWithQuantityForProductInCommand() {
    return Arrays.asList(
            new ProductWithQuantityDto(Long.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(Long.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(Long.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(Long.valueOf(4), "test", "photo", 1D, 4, true)
    );
  }

  /**
   * Fake données d'une commande
   * @param storeId BigInteger - Identifiant commerce
   * @param commandId BigInteger  - Identitifant command
   * @param slotTimeRequest LocalDateTime - Créneau de réseau
   * @return  CommandEntity
   */
  private CommandEntity getFakeCommandEntity(Long storeId, Long commandId, LocalDateTime slotTimeRequest) {
    // Fake liste d eproduit avec quantité
    CommandEntity command = new CommandEntity();

    command.setCommandProducts(fakeCommandProducList());
    command.setClientPhone("0623274100");
    command.setSlotTime(slotTimeRequest);

    // Fake calcul commande
    command.setCommandCode("vvvv");
    command.setCommandPrice(150D);
    command.setPreparationTime(50);
    command.setProductQuantity(8);

    RegisterCommandDto registerCommand = new RegisterCommandDto();
    command.setId(commandId);
    command.setStore(Factory.getStore(storeId));

    return command;
  }

  /**
   * Fake données d'une commande enregistré
   * @param storeId BigInteger - Identifiant commerce
   * @param command CommandEntity  - Fake d'une commande
   * @return RegisterCommandDto
   */
  private RegisterCommandDto getFakeRegisterCommand(Long storeId, CommandEntity command) {
    // Fake liste d eproduit avec quantité
    ManualCommandInformation manualCommandInformation = new ManualCommandInformation();
    manualCommandInformation.setSelectProducts(fakeProductWithQuantityForProductInCommand());
    manualCommandInformation.setPhoneClient(command.getClientPhone());
    manualCommandInformation.setSlotTime(command.getSlotTime());

    // Fake calcul commande
    CalculatedCommandInformation calculatedCommandInformation = new CalculatedCommandInformation();
    calculatedCommandInformation.setCommandCode(command.getCommandCode());
    calculatedCommandInformation.setCommandePrice(command.getCommandPrice());
    calculatedCommandInformation.setCommandPreparationTime(command.getPreparationTime());

    RegisterCommandDto registerCommand = new RegisterCommandDto();
    registerCommand.setCommandId(command.getId());
    registerCommand.setStoreId(storeId);
    registerCommand.setCalculatedCommandInformation(calculatedCommandInformation);
    registerCommand.setManualCommandInformation(manualCommandInformation);

    return registerCommand;
  }

  /**
   * Recherche ProductWithQuantityDto dans une liste de ProductWithQuantityDto a partir de son id
   * @param id BigInteger - id produtc
   * @param storeProducts  List<ProductWithQuantityDto> - Liste des ProductWithQuantityDto
   * @return ProductWithQuantityDto
   */
  private ProductWithQuantityDto getProductWithQuantityById(Long id, List<ProductWithQuantityDto> storeProducts) {
    return storeProducts.stream().filter(product->id.equals(product.productId())).findFirst().orElse(null);
  }

  /**
   * Renvoie la prochaine date d'un jour demandé
   * @param targetWeekDayNumber int - N° du jour voulu
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

  /**
   * Fake données d'un store
   * @param storeId BigInteger - Identitifant commerce
   * @return StoreEntity
   */
  StoreEntity getFakeStore(Long storeId) {
    StoreEntity store = new StoreEntity();
    store.setId(storeId);
    store.setFrequenceSlotTime(10);
    return store;
  }

  /**
   * Horaires commerce sur une journée avec 2 créneaux
   * @param day WeekDayEntity
   * @return List<StoreDayScheduleEntity>
   */
  List<StoreDayScheduleEntity> getFakeDoubleStoreSchedule(WeekDayEntity day, StoreEntity store) {
    StoreDayScheduleEntity storeScheduleMorning = new StoreDayScheduleEntity();
    storeScheduleMorning.setWeekDay(day);
    storeScheduleMorning.setStore(store);
    storeScheduleMorning.setOpeningTime(LocalTime.of(11,30, 0));
    storeScheduleMorning.setClosingTime(LocalTime.of(14,30,0));

    StoreDayScheduleEntity storeScheduleAfternoon = new StoreDayScheduleEntity();
    storeScheduleAfternoon.setWeekDay(day);
    storeScheduleAfternoon.setStore(store);
    storeScheduleAfternoon.setOpeningTime(LocalTime.of(18,30, 0));
    storeScheduleAfternoon.setClosingTime(LocalTime.of(22,30,0));

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
   * Fake commandes enregistrées d'un commerce
   * @param date LocalDate - Date des commandes
   * @return  List<CommandEntity>
   */
  private List<CommandEntity> getFakeCommands(LocalDate date) {
    CommandEntity command1 = new CommandEntity();
    CommandEntity command2 = new CommandEntity();
    CommandEntity command3 = new CommandEntity();
    CommandEntity command4 = new CommandEntity();
    CommandEntity command5 = new CommandEntity();
    CommandEntity command6 = new CommandEntity();

    command1.setPreparationTime(15);
    command1.setSlotTime(date.atTime(12,20,00));

    command2.setPreparationTime(10);
    command2.setSlotTime(date.atTime(12,40,00));

    command3.setPreparationTime(12);
    command3.setSlotTime(date.atTime(12,50,00));

    command4.setPreparationTime(10);
    command4.setSlotTime(date.atTime(18,40,00));

    command5.setPreparationTime(10);
    command5.setSlotTime(date.atTime(18,50,00));

    command6.setPreparationTime(10);
    command6.setSlotTime(date.atTime(19,40,00));


    return Arrays.asList(command1, command2, command3, command4, command5, command6 );
  }

  /**
   * Fake Renvoie une fausse liste de produits dans une commande
   * @return List<ProductWithQuantity>
   */
  private List<ProductWithQuantity> getFakeProductWithQuantityList() {
    ProductWithQuantity productWithQuantity1 = new ProductWithQuantity(Long.valueOf(1), 2);
    ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(Long.valueOf(2),2);
    ProductWithQuantity productWithQuantity3 = new ProductWithQuantity(Long.valueOf(3),1);

    return Arrays.asList(productWithQuantity1, productWithQuantity2, productWithQuantity3);


  }

  /**
   * Fake Renvoie une fausse liste de produits dans une commande
   * @return List<ProductWithQuantity>
   */
  private List<ProductWithQuantity> getFakeProductWithoutQuantityList() {
    ProductWithQuantity productWithQuantity1 = new ProductWithQuantity(Long.valueOf(1), 0);
    ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(Long.valueOf(2),2);
    ProductWithQuantity productWithQuantity3 = new ProductWithQuantity(Long.valueOf(3),1);

    return Arrays.asList(productWithQuantity1, productWithQuantity2, productWithQuantity3);


  }

  /**
   * Fake d'un produit n'appartenant au commerce
   * @param productId BigInteger - Identifiant Produit
   * @return ProductEntity
   */
  private ProductEntity getFakeProductNotBelongToStore(Long productId) {
    ProductEntity product1 = new ProductEntity(productId);
    product1.setStore(new StoreEntity(Long.valueOf(10)));
    product1.setName("coco");
    product1.setIsAvail(true);

    ProductEntity product2 = new ProductEntity(productId);
    product2.setStore(new StoreEntity(Long.valueOf(10)));
    product2.setIsAvail(true);

    ProductEntity product3 = new ProductEntity(productId);
    product3.setStore(new StoreEntity(Long.valueOf(10)));
    product3.setIsAvail(true);

    ProductEntity product4 = new ProductEntity(productId);
    product4.setStore(new StoreEntity(Long.valueOf(10)));
    product4.setIsAvail(true);


    List<ProductEntity> storeProducts = Arrays.asList(
          product1, product2, product3, product4
    );

    return storeProducts.stream().filter(product->productId.equals(product.getId())).findFirst().orElse(null);
  }

  /**
   * Fake d'un produit n'ayant pas de dsiponiobilité
   * @param productId BigInteger - Identifiant Produit
   * @return ProductEntity
   */
  private ProductEntity getFakeProductNotAvailable(Long productId) {
    ProductEntity product1 = new ProductEntity(productId);
    product1.setStore(new StoreEntity(Long.valueOf(1)));
    product1.setName("coco");
    product1.setIsAvail(false);

    ProductEntity product2 = new ProductEntity(productId);
    product2.setStore(new StoreEntity(Long.valueOf(10)));
    product2.setIsAvail(true);

    ProductEntity product3 = new ProductEntity(productId);
    product3.setStore(new StoreEntity(Long.valueOf(10)));
    product3.setIsAvail(true);

    ProductEntity product4 = new ProductEntity(productId);
    product4.setStore(new StoreEntity(Long.valueOf(10)));
    product4.setIsAvail(true);


    List<ProductEntity> storeProducts = Arrays.asList(
            product1, product2, product3, product4
    );

    return storeProducts.stream().filter(product->productId.equals(product.getId())).findFirst().orElse(null);
  }

  /**
   * Fake slot availibility
   * @return List<LocalDateTime>
   */
  private List<LocalDateTime> getFakeSlotAvailibility(LocalDate commandDate) {
    return Arrays.asList(
            commandDate.atTime(10,30),
            commandDate.atTime(10,35),
            commandDate.atTime(10,40),
            commandDate.atTime(10,45),
            commandDate.atTime(10,50),
            commandDate.atTime(10,55)
    );
  }

}

