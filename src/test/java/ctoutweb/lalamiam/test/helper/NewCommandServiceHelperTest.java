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
import ctoutweb.lalamiam.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewCommandServiceHelperTest {

  @Test
  void getStoreProductsWithCommandQuantity_without_existing_command() {
    Long storeId = Long.valueOf(1);

    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService);

    // Mock CommandProduct
    when(productService.getStoreProducts(any())).thenReturn(fakeProductList());

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
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService);

    // Mock CommandProduct
    when(productService.getStoreProducts(any())).thenReturn(fakeProductList());


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
    Assertions.assertEquals(1, getProductWithQuantityById(Long.valueOf(1), storeProducts).selectQuantity());
    Assertions.assertEquals(2, getProductWithQuantityById(Long.valueOf(2), storeProducts).selectQuantity());
    Assertions.assertEquals(3, getProductWithQuantityById(Long.valueOf(3), storeProducts).selectQuantity());
    Assertions.assertEquals(4, getProductWithQuantityById(Long.valueOf(4), storeProducts).selectQuantity());
  }
  @Test
  void getStoreProductsWithCommandQuantity_when_no_product_in_store_command_not_exist() {

    /**
     * Given
     */
    // Données pour ManualCommandInformation
    Long storeId = Long.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService);

    // Mock CommandProduct
    when(productService.getStoreProducts(any())).thenReturn(Arrays.asList());


    /**
     * When
     */
    List<ProductWithQuantityDto> storeProducts = commandServiceHelper.getStoreProductsWithCommandQuantity(
            storeId,
            null
    );

    /**
     * Then
     */
    Assertions.assertEquals(0, storeProducts.size());

  }
  @Test
  void getStoreProductsWithCommandQuantity_when_no_product_in_store_existing_command() {

    /**
     * Given
     */
    // Données pour ManualCommandInformation
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService);

    // Mock CommandProduct
    when(productService.getStoreProducts(any())).thenReturn(Arrays.asList());


    /**
     * When
     */
    List<ProductWithQuantityDto> storeProducts = commandServiceHelper.getStoreProductsWithCommandQuantity(
            storeId,
            getFakeRegisterCommand(storeId, null)
    );

    /**
     * Then
     */
    Assertions.assertEquals(0, storeProducts.size());

  }
  @Test
  void findRegisterCommandInformation_without_existing_command() {
    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductQuantityMapper productQuantityMapper = mock(ProductQuantityMapper.class);
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService);

    RegisterCommandDto registerCommand = commandServiceHelper.findRegisterCommandInformation(
            null
    );

    Assertions.assertNull(registerCommand);
  }
  @Test
  void findRegisterCommandInformation_with_existing_command() {
    /**
     * Given
     */
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);

    CommandTransactionSession commandTransactionSession = mock(CommandTransactionSession.class);
    ProductService productService = mock(ProductService.class);

    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    when(commandTransactionSession.getCommand(any(Long.class))).thenReturn(
            getFakeCommandEntity(storeId, commandId)
    );

    /**
     * When
     */
    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService);

    RegisterCommandDto registerCommand = commandServiceHelper.findRegisterCommandInformation(
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

    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(Long.valueOf(1))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(1))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(2))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(2))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(3))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(3))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(4))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(4))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    Integer commandPreparationTime = commandServiceHelper.calculateCommandPreparationTime(getFakeProductList());

    Assertions.assertEquals(27, commandPreparationTime);
  }
  @Test
  void calculateCommandPreparationTime_without_products() {

    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    Integer commandPreparationTime = commandServiceHelper.calculateCommandPreparationTime(Arrays.asList());

    Assertions.assertNull(commandPreparationTime);
  }
  @Test
  void calculateCommandPrice_with_products() {

    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(Long.valueOf(1))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(1))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(2))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(2))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(3))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(3))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(4))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(4))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    Double commandPrice = commandServiceHelper.calculateCommandPrice(getFakeProductList());

    Assertions.assertEquals(37D, commandPrice);
  }
  @Test
  void calculateCommandPrice_without_products() {

    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    Double commandPrice = commandServiceHelper.calculateCommandPrice(Arrays.asList());

    Assertions.assertNull(commandPrice);
  }
  @Test
  void calculateNumberOfProductInCommand_with_products() {
    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);
    when(productService.findProduct(Long.valueOf(1))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(1))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(2))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(2))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(3))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(3))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    when(productService.findProduct(Long.valueOf(4))).thenReturn(
            fakeStoreProductEntiy()
                    .stream()
                    .filter(product -> product.getId().equals(Long.valueOf(4))).findFirst()
                    .orElseThrow(()->new RuntimeException("")));

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    Integer numberOfProductInCommand = commandServiceHelper.calculateNumberOfProductInCommand(getFakeProductList());

    Assertions.assertEquals(5, numberOfProductInCommand);
  }
  @Test
  void calculateNumberOfProductInCommand_without_products() {
    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    Integer numberOfProductInCommand = commandServiceHelper.calculateNumberOfProductInCommand(Arrays.asList());

    Assertions.assertNull(numberOfProductInCommand);
  }
  @Test
  void generateCode() {
    // ProductQuantityMapper
    ProductQuantityMapper productQuantityMapper = new ProductQuantityMapper();

    // Mock ProductService
    ProductService productService = mock(ProductService.class);

    NewCommandServiceHelper commandServiceHelper = new NewCommandServiceHelper(
            productQuantityMapper,
            productService
    );

    String generateCode6CaracLength = commandServiceHelper.generateCode(6);
    String generateCode7CaracLength = commandServiceHelper.generateCode(7);

    Assertions.assertEquals(6, generateCode6CaracLength.length());
    Assertions.assertEquals(7, generateCode7CaracLength.length());



  }
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Renvoie un mock d'une liste de produit d'un commerce
   */
  private List<ProductEntity> fakeProductList() {
    return Arrays.asList(
            new ProductEntity(Long.valueOf(1)),
            new ProductEntity(Long.valueOf(2)),
            new ProductEntity(Long.valueOf(3)),
            new ProductEntity(Long.valueOf(4))
    );
  }

  /**
   * Fake Renvoie d'une liste de produit a afficher coté client
   * @return List<ProductWithQuantityDto>
   */
  private List<ProductWithQuantityDto> fakeProductWithQuantityDto() {
    return Arrays.asList(
            new ProductWithQuantityDto(Long.valueOf(1), "test", "photo", 1D, 1, true),
            new ProductWithQuantityDto(Long.valueOf(2), "test", "photo", 1D, 2, true),
            new ProductWithQuantityDto(Long.valueOf(3), "test", "photo", 1D, 3, true),
            new ProductWithQuantityDto(Long.valueOf(4), "test", "photo", 1D, 4, true)
    );
  }

  /**
   * Renvoie une fausse liste de commande
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
   * Renvoie un produit
   * @return ProductEntity
   */
  private ProductEntity fakeProduct() {
    return new ProductEntity(Long.valueOf(1));
  }

  /**
   * Renvoie l'ensemble des données d'une commande pour affichage cotés client
   * @param storeId BigInteger
   * @param commandId BigInteger
   * @return RegisterCommandDto
   */
  private RegisterCommandDto getFakeRegisterCommand(Long storeId, Long commandId) {
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
  private CommandEntity getFakeCommandEntity(Long storeId, Long commandId) {
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
  private ProductWithQuantityDto getProductWithQuantityById(Long id, List<ProductWithQuantityDto> storeProducts) {
    return storeProducts.stream().filter(product->id.equals(product.productId())).findFirst().orElse(null);
  }


  /**
   * Fake Renvoie une fausse liste de produits dans une commande
   * @return List<ProductWithQuantity>
   */
  private List<ProductWithQuantity> getFakeProductList() {
    ProductWithQuantity productWithQuantity1 = new ProductWithQuantity(Long.valueOf(1), 2);
    ProductWithQuantity productWithQuantity2 = new ProductWithQuantity(Long.valueOf(2),2);
    ProductWithQuantity productWithQuantity3 = new ProductWithQuantity(Long.valueOf(3),1);

    return Arrays.asList(productWithQuantity1, productWithQuantity2, productWithQuantity3);
  }

  /**
   * Fake prioduits d'un commerce
   * @return List<ProductEntity>
   */
  private List<ProductEntity> fakeStoreProductEntiy() {
    return Arrays.asList(
            new ProductEntity(Long.valueOf(1), "test", 10D,  "photo", 4, "photo", true),
            new ProductEntity(Long.valueOf(2), "test", 5D,  "photo", 7, "photo", true),
            new ProductEntity(Long.valueOf(3), "test", 7D,  "photo", 5, "photo", true),
            new ProductEntity(Long.valueOf(4), "test", 10D,  "photo", 6, "photo", true),
            new ProductEntity(Long.valueOf(5), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(6), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(7), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(8), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(9), "test", 15D,  "photo", 10, "photo", true),
            new ProductEntity(Long.valueOf(10), "test", 15D,  "photo", 10, "photo", true)
    );
  }
}
