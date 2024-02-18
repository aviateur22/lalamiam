package ctoutweb.lalamiam.test.helper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.NewCommandServiceHelper;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.CalculatedCommandInformation;
import ctoutweb.lalamiam.model.ManualCommandInformation;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.service.CommandProductService;
import ctoutweb.lalamiam.service.ProductService;
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
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper,
            productService);

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
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper,
            productService);

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
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper,
            productService);

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
    ProductService productService = mock(ProductService.class);

    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    when(commandTransactionSession.getCommand(any(BigInteger.class))).thenReturn(
            getFakeCommandEntity(storeId, commandId)
    );

    /**
     * When
     */
    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper,
            productService);

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
  @Test
  void calculateCommandPreparationTime_with_products() {

    // Mock CommandProductService
    CommandProductService commandProductService = mock(CommandProductService.class);

    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(BigInteger.valueOf(1))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(BigInteger.valueOf(1))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(BigInteger.valueOf(2))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(BigInteger.valueOf(2))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(BigInteger.valueOf(3))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(BigInteger.valueOf(3))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(BigInteger.valueOf(4))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(BigInteger.valueOf(4))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper,
            productService
    );

    Integer commandPreparationTime = commandServiceHelper.calculateCommandPreparationTime(getFakeProductList());

    Assertions.assertEquals(27, commandPreparationTime);
  }

  @Test
  void calculateCommandPreparationTime_without_products() {

    // Mock CommandProductService
    CommandProductService commandProductService = mock(CommandProductService.class);

    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            commandProductService,
            productQuantityMapper,
            productService
    );

    Integer commandPreparationTime = commandServiceHelper.calculateCommandPreparationTime(Arrays.asList());

    Assertions.assertNull(commandPreparationTime);
  }
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

  /**
   * Fake Renvoie d'une liste de produit a afficher coté client
   * @return List<ProductWithQuantityDto>
   */
  private List<ProductWithQuantityDto> fakeProductWithQuantityDto() {
    return Arrays.asList(
            new ProductWithQuantityDto(BigInteger.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(BigInteger.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(BigInteger.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(BigInteger.valueOf(4), "test", "photo", 1D, 4, true)
    );
  }

  /**
   * Renvoie une fausse liste de commande
   * @return List<CommandProductEntity>
   */
  private List<CommandProductEntity> fakeCommandProducList() {
    return Arrays.asList(
            new CommandProductEntity(1, new ProductEntity(BigInteger.valueOf(1),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(2, new ProductEntity(BigInteger.valueOf(2),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(3, new ProductEntity(BigInteger.valueOf(3),"test", 1D , "description", 5,"photo", true)),
            new CommandProductEntity(4, new ProductEntity(BigInteger.valueOf(4),"test", 1D , "description", 5,"photo", true))
    );
  }

  /**
   * Renvoie un produit
   * @return ProductEntity
   */
  private ProductEntity fakeProduct() {
    return new ProductEntity(BigInteger.valueOf(1));
  }

  /**
   * Renvoie l'ensemble des données d'une commande pour affichage cotés client
   * @param storeId BigInteger
   * @param commandId BigInteger
   * @return RegisterCommandDto
   */
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

  /**
   * Renvoie les données d'une commande (Brut de la base de données)
   * @param storeId BigInteger
   * @param commandId BigInteger
   * @return CommandEntity
   */
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

  /**
   * Récupération d'un ProductWithQuantityDto dans une list de ProductWithQuantityDto
   * @param id Integer - id du ProductWithQuantityDto a récupérer
   * @param storeProducts List<ProductWithQuantityDto>
   * @return ProductWithQuantityDto
   */
  private ProductWithQuantityDto getProductWithQuantityById(BigInteger id, List<ProductWithQuantityDto> storeProducts) {
    return storeProducts.stream().filter(product->id.equals(product.productId())).findFirst().orElse(null);
  }


  /**
   * Fake Renvoie une fausse liste de produits dans une commande
   * @return List<ProductWithQuantity>
   */
  private List<ProductWithQuantity> getFakeProductList() {
    ProductWithQuantity productWithQuantity1 = new ProductWithQuantity(BigInteger.valueOf(1), 2);
    ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(BigInteger.valueOf(2),2);
    ProductWithQuantity productWithQuantity3 = new ProductWithQuantity(BigInteger.valueOf(3),1);

    return Arrays.asList(productWithQuantity1, productWithQuantity2, productWithQuantity3);
  }

  /**
   * Fake prioduits d'un commerce
   * @return List<ProductEntity>
   */
  private List<ProductEntity> fakeStoreProductEntiy() {
    return Arrays.asList(
            new ProductEntity(BigInteger.valueOf(1), "test", 15D,  "photo", 4, "photo", true),
            new ProductEntity(BigInteger.valueOf(2), "test", 15D,  "photo", 7, "photo", true),
            new ProductEntity(BigInteger.valueOf(3), "test", 15D,  "photo", 5, "photo", true),
            new ProductEntity(BigInteger.valueOf(4), "test", 15D,  "photo", 6, "photo", true),
            new ProductEntity(BigInteger.valueOf(5), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(6), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(7), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(8), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(9), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(BigInteger.valueOf(10), "test", 15D,  "photo", 10, "photo", true)
    );
  }
}
