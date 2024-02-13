package ctoutweb.lalamiam.test.service;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.CalculatedCommandInformation;
import ctoutweb.lalamiam.model.ManualCommandInformation;
import ctoutweb.lalamiam.model.dto.CommandInformationDto;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.CommandProductService;
import ctoutweb.lalamiam.service.serviceImpl.NewCommandServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NewCommandServiceTest {

NewCommandServiceImp newCommandService;

  @Test
  void getStoreProductsForCommand_with_command_and_store_produst() {
    BigInteger storeId = BigInteger.valueOf(1);
    BigInteger commandId = BigInteger.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    CommandProductService commandProductService = mock(CommandProductService.class);
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();
    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(commandProductService, productQuantityMapper);

    newCommandService = new NewCommandServiceImp(commandServiceHelper, commandTransactionSession);

    /**
     * When
     */
    // Fake CommandEntity
    CommandEntity command = getFakeCommandEntity(storeId, commandId);
    when(commandTransactionSession.getCommand(any(BigInteger.class))).thenReturn(command);

    // Fake List<ProductEntity>
    when(commandProductService.getStoreProducts(any(BigInteger.class))).thenReturn(fakeStoreProductEntiy());

    CommandInformationDto commandInformation = newCommandService.getStoreProductsForCommand(storeId, commandId);

    /**
     * Then
     */
    Assertions.assertEquals("0623274100", commandInformation.clientPhone());
    Assertions.assertEquals(command.getSlotTime(), commandInformation.slotTime());
    Assertions.assertEquals(fakeStoreProductEntiy().size(), commandInformation.storeProducts().size());
    Assertions.assertEquals(1, getProductWithQuantityById(BigInteger.valueOf(1), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(2, getProductWithQuantityById(BigInteger.valueOf(2), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(3, getProductWithQuantityById(BigInteger.valueOf(3), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(4, getProductWithQuantityById(BigInteger.valueOf(4), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(5), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(6), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(7), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(8), commandInformation.storeProducts()).selectQuantity());
  }

  @Test
  void getStoreProductsForCommand_without_command() {
    // TODO Mettre a jour le test unitaire
    BigInteger storeId = BigInteger.valueOf(1);
    BigInteger commandId = BigInteger.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    CommandProductService commandProductService = mock(CommandProductService.class);
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();
    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(commandProductService, productQuantityMapper);

    newCommandService = new NewCommandServiceImp(commandServiceHelper, commandTransactionSession);

    /**
     * When
     */
    // Fake CommandEntity
    CommandEntity command = null;
    when(commandTransactionSession.getCommand(any(BigInteger.class))).thenReturn(command);

    // Fake List<ProductEntity>
    when(commandProductService.getStoreProducts(any(BigInteger.class))).thenReturn(fakeStoreProductEntiy());

    CommandInformationDto commandInformation = newCommandService.getStoreProductsForCommand(storeId, commandId);

    /**
     * Then
     */
    Assertions.assertEquals(null, commandInformation.clientPhone());
    Assertions.assertEquals(null, commandInformation.slotTime());
    Assertions.assertEquals(fakeStoreProductEntiy().size(), commandInformation.storeProducts().size());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(1), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(2), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(3), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(4), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(5), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(6), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(7), commandInformation.storeProducts()).selectQuantity());
    Assertions.assertEquals(0, getProductWithQuantityById(BigInteger.valueOf(8), commandInformation.storeProducts()).selectQuantity());
  }
  @Test
  void getStoreProductsForCommand_with_command_not_belong_to_store() {
    BigInteger storeId = BigInteger.valueOf(1);
    BigInteger commandId = BigInteger.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    CommandProductService commandProductService = mock(CommandProductService.class);
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();
    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(commandProductService, productQuantityMapper);

    newCommandService = new NewCommandServiceImp(commandServiceHelper, commandTransactionSession);

    /**
     * When
     */
    // Fake CommandEntity
    CommandEntity command = getFakeCommandEntity(BigInteger.valueOf(2), commandId);
    when(commandTransactionSession.getCommand(any(BigInteger.class))).thenReturn(command);

    // Fake List<ProductEntity>
    when(commandProductService.getStoreProducts(any(BigInteger.class))).thenReturn(fakeStoreProductEntiy());

    /**
     * Then
     */
    Exception exception = Assertions.assertThrows(
            RuntimeException.class, ()->newCommandService.getStoreProductsForCommand(storeId, commandId)
    );

    Assertions.assertThrows(RuntimeException.class, ()->newCommandService.getStoreProductsForCommand(storeId, commandId));
    Assertions.assertEquals("Cette commande n'est pas rattaché au commerce".toLowerCase(), exception.getMessage().toLowerCase());
  }

  private List<CommandProductEntity> fakeCommandProducList() {
    return Arrays.asList(
            new CommandProductEntity(1, new ProductEntity(BigInteger.valueOf(1),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(2, new ProductEntity(BigInteger.valueOf(2),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(3, new ProductEntity(BigInteger.valueOf(3),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(4, new ProductEntity(BigInteger.valueOf(4),"test", 1D , "description", 5,"photo", true))
    );
  }
  private List<ProductWithQuantityDto> fakeProductWithQuantityForStore() {
    return Arrays.asList(
            new ProductWithQuantityDto(BigInteger.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(BigInteger.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(BigInteger.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(4), "test", "photo", 1D, 4, true),
            new ProductWithQuantityDto(BigInteger.valueOf(5), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(6), "test", "photo", 1D, 4, true),
            new ProductWithQuantityDto(BigInteger.valueOf(7), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(8), "test", "photo", 1D, 4, true),
            new ProductWithQuantityDto(BigInteger.valueOf(9), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(10), "test", "photo", 1D, 4, true)
    );
  }
  private List<ProductEntity> fakeStoreProductEntiy() {
    return Arrays.asList(
            new ProductEntity(BigInteger.valueOf(1), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(2), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(3), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(4), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(5), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(6), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(7), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(8), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(9), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(10), "test", 15D,  "photo", 10, "photo", true)
    );
  }
  private List<ProductWithQuantityDto> fakeProductWithQuantityForProductInCommand() {
    return Arrays.asList(
            new ProductWithQuantityDto(BigInteger.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(BigInteger.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(BigInteger.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(4), "test", "photo", 1D, 4, true)
    );
  }
  private CommandEntity getFakeCommandEntity(BigInteger storeId, BigInteger commandId) {
    // Fake liste d eproduit avec quantité
    CommandEntity command = new CommandEntity();

    command.setCommandProducts(fakeCommandProducList());
    command.setClientPhone("0623274100");
    command.setSlotTime(LocalDateTime.now());

    // Fake calcul commande
    command.setCommandCode("vvvv");
    command.setCommandPrice(150D);
    command.setPreparationTime(50);

    RegisterCommandDto registerCommand = new RegisterCommandDto();
    command.setId(commandId);
    command.setStore(Factory.getStore(storeId));

    return command;
  }
  private RegisterCommandDto getFakeRegisterCommand(BigInteger storeId, CommandEntity command) {
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
  private ProductWithQuantityDto getProductWithQuantityById(BigInteger id, List<ProductWithQuantityDto> storeProducts) {
    return storeProducts.stream().filter(product->id.equals(product.productId())).findFirst().orElse(null);
  }

}
