package ctoutweb.lalamiam.test.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.CalculatedCommandInformation;
import ctoutweb.lalamiam.model.ManualCommandInformation;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.CommandProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewCommandServiceHelperTest {

  @Test
  void getStoreProductsWithCommandQuantity_without_existing_command() {
    BigInteger storeId = BigInteger.valueOf(1);

    CommandProductService commandProductService = mock(CommandProductService.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper
    );

    // Mock CommandProduct
    when(commandProductService.getStoreProducts(any())).thenReturn(fakeProductList());

    RegisterCommandDto registerCommand = null;

    // Appelle de la focntion
    List<ProductWithQuantityDto> storeProducts = commandServiceHelper.getStoreProductsWithCommandQuantity(storeId, registerCommand);

    Assertions.assertEquals(4, storeProducts.size());
    Assertions.assertEquals(0, storeProducts.get(0).selectQuantity());
    Assertions.assertEquals(0, storeProducts.get(1).selectQuantity());
    Assertions.assertEquals(0, storeProducts.get(2).selectQuantity());
    Assertions.assertEquals(0, storeProducts.get(3).selectQuantity());
  }

  @Test
  void getStoreProductsWithCommandQuantity_with_existing_command() {

    /**
     * Given
     */
    // Données pour ManualCommandInformation
    BigInteger storeId = BigInteger.valueOf(1);
    BigInteger commandId = BigInteger.valueOf(1);

    CommandProductService commandProductService = mock(CommandProductService.class);
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper
    );

    // Mock CommandProduct
    when(commandProductService.getStoreProducts(any())).thenReturn(fakeProductList());


    /**
     * When
     */
    List<ProductWithQuantityDto> storeProducts = commandServiceHelper.getStoreProductsWithCommandQuantity(
            storeId,
            getFakeRegisterCommand(storeId, commandId)
    );

    /**
     * Then
     */
    Assertions.assertEquals(4, storeProducts.size());
    Assertions.assertEquals(1, getProductWithQuantityById(BigInteger.valueOf(1), storeProducts).selectQuantity());
    Assertions.assertEquals(2, getProductWithQuantityById(BigInteger.valueOf(2), storeProducts).selectQuantity());
    Assertions.assertEquals(3, getProductWithQuantityById(BigInteger.valueOf(3), storeProducts).selectQuantity());
    Assertions.assertEquals(4, getProductWithQuantityById(BigInteger.valueOf(4), storeProducts).selectQuantity());
  }

  @Test
  void findRegisterCommandInformation_without_existing_command() {

    CommandProductService commandProductService = mock(CommandProductService.class);
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper
    );

    RegisterCommandDto registerCommand = commandServiceHelper.findRegisterCommandInformation(
            BigInteger.valueOf(1),
            null
    );

    Assertions.assertNull(registerCommand);
  }

  @Test
  void findRegisterCommandInformation_with_existing_command() {
    /**
     * Given
     */
    BigInteger storeId = BigInteger.valueOf(1);
    BigInteger commandId = BigInteger.valueOf(1);

    CommandProductService commandProductService = mock(CommandProductService.class);
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    when(commandTransactionSession.getCommand(any(BigInteger.class))).thenReturn(
            getFakeCommandEntity(storeId, commandId)
    );

    /**
     * When
     */
    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,

            productQuantityMapper
    );
    RegisterCommandDto registerCommand = commandServiceHelper.findRegisterCommandInformation(
            storeId,
            getFakeCommandEntity(storeId, commandId)
    );

    /**
     * Then
     */
    Assertions.assertEquals(storeId, registerCommand.getStoreId());
    Assertions.assertEquals(commandId, registerCommand.getCommandId());
    Assertions.assertEquals(fakeProductWithQuantityDto(), registerCommand.getManualCommandInformation().getSelectProducts());
  }


  /**
   * Renvoie un mock d'une liste de produit d'un commerce
   */
  private List<ProductEntity> fakeProductList() {
    return Arrays.asList(
            new ProductEntity(BigInteger.valueOf(1)),
            new ProductEntity(BigInteger.valueOf(2)),
            new ProductEntity(BigInteger.valueOf(3)),
            new ProductEntity(BigInteger.valueOf(4))
    );
  }

  private List<ProductWithQuantityDto> fakeProductWithQuantityDto() {
    return Arrays.asList(
            new ProductWithQuantityDto(BigInteger.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(BigInteger.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(BigInteger.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(4), "test", "photo", 1D, 4, true)
    );
  }

  private List<CommandProductEntity> fakeCommandProducList() {
    return Arrays.asList(
            new CommandProductEntity(1, new ProductEntity(BigInteger.valueOf(1),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(2, new ProductEntity(BigInteger.valueOf(2),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(3, new ProductEntity(BigInteger.valueOf(3),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(4, new ProductEntity(BigInteger.valueOf(4),"test", 1D , "description", 5,"photo", true))
    );
  }
  private ProductEntity fakeProduct() {
    return new ProductEntity(BigInteger.valueOf(1));
  }
  private RegisterCommandDto getFakeRegisterCommand(BigInteger storeId, BigInteger commandId) {
    // Fake liste d eproduit avec quantité
    ManualCommandInformation manualCommandInformation = new ManualCommandInformation();
    manualCommandInformation.setSelectProducts(fakeProductWithQuantityDto());
    manualCommandInformation.setPhoneClient("0623274100");
    manualCommandInformation.setSlotTime(LocalDateTime.now());

    // Fake calcul commande
    CalculatedCommandInformation calculatedCommandInformation = new CalculatedCommandInformation();
    calculatedCommandInformation.setCommandCode("vvvv");
    calculatedCommandInformation.setCommandePrice(150D);
    calculatedCommandInformation.setCommandPreparationTime(50);

    RegisterCommandDto registerCommand = new RegisterCommandDto();
    registerCommand.setCommandId(commandId);
    registerCommand.setStoreId(storeId);
    registerCommand.setCalculatedCommandInformation(calculatedCommandInformation);
    registerCommand.setManualCommandInformation(manualCommandInformation);

    return registerCommand;
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

  private ProductWithQuantityDto getProductWithQuantityById(BigInteger id, List<ProductWithQuantityDto> storeProducts) {
    return storeProducts.stream().filter(product->id.equals(product.productId())).findFirst().orElse(null);
  }

}
