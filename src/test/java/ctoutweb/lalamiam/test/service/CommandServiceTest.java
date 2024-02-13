package ctoutweb.lalamiam.test.service;

import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.builder.StoreEntityBuilder;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.service.*;
import helper.*;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//TODO vérifier qu'une commande ne puisse pas etre supprimé ou modifié par un store exterieur
@SpringBootTest
public class CommandServiceTest {

  @Autowired
  ProRepository proRepository;

  @Autowired
  CommandService commandService;

  @Autowired
  CommandProductService commandProductService;

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

  StoreHelper storeHelper;

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
    Exception exception = Assertions.assertThrows(RuntimeException.class,  ()->commandProductService.addProductsInCommand(addProductsInCommandSchema));

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
    AddProductsInCommandResponseDto commandDetail = commandProductService.addProductsInCommand(addProductsInCommandSchema);

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


}
