package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.model.dto.CommandDetailDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CookRepository;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.service.CommandService;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.ProductService;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
  CookRepository cookRepository;

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
    proRepository.truncateAll();
    commandRepository.deleteAll();
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
    CommandDetailDto addCommand = commandService.addCommand(addCommandSchema());

    // Controle de la commande
    Assertions.assertEquals(1, commandRepository.countAll());
    //Assertions.assertEquals(addCommand.phoneClient(), addCommand.getClientPhone());
    //Assertions.assertEquals(addCommand.getPreparationTime(), addCommand.getPreparationTime());

    // Controle relation produits - store - commande
    Assertions.assertEquals(3, cookRepository.countAll());
    List<CookEntity> cooks = cookRepository.findAll();

    Assertions.assertEquals(productsInCommand.get(0).productId(), cooks.get(0).getProduct().getId());
    Assertions.assertEquals(addCommand.commandId(), cooks.get(0).getCommand().getId());
    Assertions.assertEquals(store.getId(), cooks.get(0).getStore().getId());

    Assertions.assertEquals(productsInCommand.get(1).productId(), cooks.get(1).getProduct().getId());
    Assertions.assertEquals(addCommand.commandId(), cooks.get(1).getCommand().getId());
    Assertions.assertEquals(store.getId(), cooks.get(1).getStore().getId());

    Assertions.assertEquals(productsInCommand.get(2).productId(), cooks.get(2).getProduct().getId());
    Assertions.assertEquals(addCommand.commandId(), cooks.get(2).getCommand().getId());
    Assertions.assertEquals(store.getId(), cooks.get(2).getStore().getId());
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
    Assertions.assertEquals("Horaire de commande invalide", exception.getMessage());
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
    Assertions.assertEquals("Le produit n'est pas rattaché au store", exception.getMessage());
  }

  @Test
  void should_update_product_quantity_in_command(){
    // Creation Commande
    CommandDetailDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation d'un produit de la commande
    BigInteger productChangeId = productsInCommand.get(0).productId();

    // Modification du produit dans la commande
    UpdateProductQuantityInCommandSchema updateCommandSchema = new UpdateProductQuantityInCommandSchema(
            productChangeId, addCommand.commandId(), store.getId(), 25);
    UpdateProductQuantityInCommandDto productUpdated = commandService.updateProductQuantityInCommand(updateCommandSchema);

    Assertions.assertEquals(25, productUpdated.productInCommand().productQuantity());
    Assertions.assertEquals(productChangeId, productUpdated.productInCommand().productId());
  }

  @Test
  void should_not_update_product_quantity_if_product_not_belong_to_command() {
    // Creation Commande store1
    CommandDetailDto addCommand = commandService.addCommand(addCommandSchema());

    // Creation produit pour store2
    ProInformationDto pro2 = createPro();
    StoreEntity store2 = createStore(pro2);
    List<AddProductDto> productStore2 = createProduct(store2.getId());

    // Récuperation d'un produit du store2
    BigInteger productStore2Id = productStore2.get(0).id();

    // Modification quantité commande store1 avec un produit du store2
    UpdateProductQuantityInCommandSchema updateCommandSchema = new UpdateProductQuantityInCommandSchema(
            productStore2Id, addCommand.commandId(), store.getId(), 12);

    Exception exception = Assertions.assertThrows(
            RuntimeException.class,
            ()->commandService.updateProductQuantityInCommand(updateCommandSchema)
    );
    Assertions.assertEquals("Le produit n'est pas rattaché au commerce", exception.getMessage());


  }

  @Test
  void should_force_update_product_quantity_to_1_if_quantity_inferior_at_1() {
    // Creation Commande
    CommandDetailDto addCommand = commandService.addCommand(addCommandSchema());

    // Récuperation d'un produit de la commande
    BigInteger productChangeId = productsInCommand.get(0).productId();

    // Modification du produit dans la commande
    UpdateProductQuantityInCommandSchema updateCommandSchema = new UpdateProductQuantityInCommandSchema(
            productChangeId, addCommand.commandId(), store.getId(), 0);
    UpdateProductQuantityInCommandDto productUpdated = commandService.updateProductQuantityInCommand(updateCommandSchema);

    Assertions.assertEquals(1, productUpdated.productInCommand().productQuantity());
    Assertions.assertEquals(productChangeId, productUpdated.productInCommand().productId());
  }
  @Test
  void should_delete_one_product_in_command() {
    // Creation Commande
    CommandDetailDto addCommand = commandService.addCommand(addCommandSchema());

    // Recuperation du produit a supprimer
    BigInteger productId = addCommand.productInCommandList().get(0).productId();

    BigInteger commandId = addCommand.commandId();
    // Suppression du produit
    DeleteProductInCommandSchema deleteProductInCommand = new DeleteProductInCommandSchema(commandId, productId, store.getId());
    CommandDetailDto updateCommandDetail = commandService.deleteProductInCommand(deleteProductInCommand);

    Assertions.assertEquals(2, updateCommandDetail.productInCommandList().size());
    Assertions.assertEquals(0,
            updateCommandDetail.productInCommandList()
                    .stream()
                    .filter(product -> product.productId() == productId)
                    .collect(Collectors.toList())
                    .size());

  }
  /**
   * Création schema pour une commande
   * @return AddCommandSchema
   */
  private AddCommandSchema addCommandSchema() {

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
    StoreEntity createdStore = createStore(createdPro);
    StoreEntity createdStore2;

    store = isStoreExist ?
            createdStore :
            StoreEntityBuilder.aStoreEntity().withId(BigInteger.valueOf(0)).build();

    // Création produits
    List<AddProductDto> createProductList = createProduct(createdStore.getId());

    // Creation commande
    createProductsInCommand(createProductList);

    // Creation Pro2 + Store2 + produits2 et ajout d'un produit à la commande 1
    if(!isProdutcBelongToStore) {
      createdPro2 = createPro();
      createdStore2 = createStore(createdPro2);
      List<AddProductDto> createProductList2 = createProduct(createdStore2.getId());
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

    AddCommandSchema addCommandSchema = new AddCommandSchema(
            phoneCient,
            commandSlotTime,
            store.getId(),
            productsInCommand);

    // schema ajout commande
    return addCommandSchema;
  }

  /**
   * Liste des produits pour une commande
   * @param productList
   * @return
   */
  public void createProductsInCommand(List<AddProductDto> productList) {
    productsInCommand = productList
            .stream()
            .map(product -> new ProductWithQuantity(product.id(), 2))
            .collect(Collectors.toList());
  }


  /**
   * Creation Professionnel
   * @return ProInformationDto
   */
  public ProInformationDto createPro() {
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));
    return createdPro;
  }

  /**
   * Creation Store
   * @param createdPro - ProInformationDto - Données sur le professionnel
   * @return StoreEntity
   */
  public StoreEntity createStore(ProInformationDto createdPro) {
    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity createdStore = storeService.createStore(addStoreSchema);
    return createdStore;
  }
  public List<AddProductDto> createProduct(BigInteger storeId) {
    AddProductSchema addProductSchema1 = new AddProductSchema("lait", 10D, "initial description", 5, "s", storeId);
    AddProductSchema addProductSchema2 = new AddProductSchema("coco", 20D, "initial description", 5, "s", storeId);
    AddProductSchema addProductSchema3 = new AddProductSchema("orange", 10D, "initial description", 5, "s", storeId);

    AddProductDto addProduct1 =  productService.addProduct(addProductSchema1);
    AddProductDto addProduct2 =  productService.addProduct(addProductSchema2);
    AddProductDto addProduct3 =  productService.addProduct(addProductSchema3);

    List<AddProductDto> createdProductList = new ArrayList<>();
    createdProductList.add(addProduct1);
    createdProductList.add(addProduct2);
    createdProductList.add(addProduct3);

    return createdProductList;
  }

}
