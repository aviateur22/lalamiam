package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.ProductService;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CommandServiceHelperTest {

  @Autowired
  ProService proService;

  @Autowired
  CommandServiceHelper commandServiceHelper;

  @Autowired
  CommandServiceHelper storeServiceHelper;
  @Autowired
  ProRepository proRepository;

  @Autowired
  ProductService productService;

  @Autowired
  StoreService storeService;

  @BeforeEach
  void beforeEach() {
    proRepository.truncateAll();
  }

  @Test
  void should_find_list_of_product_by_id() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));

    // Creation Store
    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produits
    AddProductSchema addProductSchema1 = new AddProductSchema("pomme",
            10D,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductSchema addProductSchema2 = new AddProductSchema("poire",
            20D,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductSchema addProductSchema3 = new AddProductSchema("bannane",
            10D,
            "initial description",
            5,
            "s",
            store.getId());

    ProductEntity addProduct1 =  productService.addProduct(addProductSchema1);
    ProductEntity addProduct2 =  productService.addProduct(addProductSchema2);
    ProductEntity addProduct3 =  productService.addProduct(addProductSchema3);

    List<BigInteger> productsId = new ArrayList<>();
    productsId.add(addProduct1.getId());
    productsId.add(addProduct2.getId());
    productsId.add(addProduct3.getId());

    Assertions.assertEquals(3, storeServiceHelper.findProductListById(productsId).size());
  }

  @Test
  void should_not_find_list_of_product_which_one_not_register() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));

    // Creation Store
    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produits
    AddProductSchema addProductSchema1 = new AddProductSchema("pomme",
            10D,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductSchema addProductSchema2 = new AddProductSchema("poire",
            20D,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductSchema addProductSchema3 = new AddProductSchema("bannane",
            10D,
            "initial description",
            5,
            "s",
            store.getId());

    ProductEntity addProduct1 =  productService.addProduct(addProductSchema1);
    ProductEntity addProduct2 =  productService.addProduct(addProductSchema2);
    ProductEntity addProduct3 =  productService.addProduct(addProductSchema3);

    // ajout des id dans une liste
    BigInteger randomProductId = BigInteger.valueOf(0);
    List<BigInteger> productsId = new ArrayList<>();
    productsId.add(addProduct1.getId());
    productsId.add(addProduct2.getId());
    productsId.add(addProduct3.getId());
    productsId.add(randomProductId);

    Assertions.assertThrows(RuntimeException.class,()-> storeServiceHelper.findProductListById(productsId).size());
  }

  @Test
  void should_calculate_command_price() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));

    // Creation Store
    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produits
    AddProductSchema addProductSchema1 = new AddProductSchema("pomme",
            1.5,
            "initial description",
            2,
            "s",
            store.getId());
    AddProductSchema addProductSchema2 = new AddProductSchema("poire",
            2.35,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductSchema addProductSchema3 = new AddProductSchema("bannane",
            10D,
            "initial description",
            10,
            "s",
            store.getId());

    ProductEntity addProduct1 =  productService.addProduct(addProductSchema1);
    ProductEntity addProduct2 =  productService.addProduct(addProductSchema2);
    ProductEntity addProduct3 =  productService.addProduct(addProductSchema3);

    List<ProductEntity> products = new ArrayList<>();
    products.add(addProduct1);
    products.add(addProduct2);
    products.add(addProduct3);

    Assertions.assertEquals(13.85, storeServiceHelper.calculateCommandPrice(products));
  }

  @Test
  void should_calculate_command_preparation_time() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));

    // Creation Store
    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produits
    AddProductSchema addProductSchema1 = new AddProductSchema("pomme",
            1.5,
            "initial description",
            2,
            "s",
            store.getId());
    AddProductSchema addProductSchema2 = new AddProductSchema("poire",
            2.35,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductSchema addProductSchema3 = new AddProductSchema("bannane",
            10D,
            "initial description",
            10,
            "s",
            store.getId());

    ProductEntity addProduct1 =  productService.addProduct(addProductSchema1);
    ProductEntity addProduct2 =  productService.addProduct(addProductSchema2);
    ProductEntity addProduct3 =  productService.addProduct(addProductSchema3);

    List<ProductEntity> products = new ArrayList<>();
    products.add(addProduct1);
    products.add(addProduct2);
    products.add(addProduct3);

    Assertions.assertEquals(17, commandServiceHelper.calculatCommandPreparationTime(products));

  }
}
