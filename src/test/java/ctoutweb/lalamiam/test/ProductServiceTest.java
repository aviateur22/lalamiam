package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.model.dto.UpdateProductDto;
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
import java.util.List;

@SpringBootTest
public class ProductServiceTest {
  @Autowired
  ProService proService;

  @Autowired
  ProductService productService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProRepository proRepository;

  @Autowired
  StoreService storeService;

  @BeforeEach
  void beforeEach() {
    proRepository.truncateAll();
  }

  @Test
  void should_add_product() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("melon", 1.9, "jolie melon", 5, "ddd", store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    /**
     * Then
     */
    List<ProductEntity> product = productRepository.findAll();
    Assertions.assertEquals(1, productRepository.countAll());
    Assertions.assertEquals(1.9, product.get(0).getPrice());
    Assertions.assertEquals("melon", product.get(0).getName());
  }

  @Test
  void should_not_add_product_if_store_not_exist()   {
    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("melon", 1.9, "jolie melon", 5, "ddd", BigInteger.valueOf(0));

    /**
     * Then
     */
    Assertions.assertThrows(RuntimeException.class, ()->productService.addProduct(addProductSchema));
    Assertions.assertEquals(0, productRepository.countAll());
  }
  @Test
  void should_not_add_product_with_decription_missing() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("melon", 0D, "", 5, "", store.getId());

    List<ProductEntity> product = productRepository.findAll();
    Assertions.assertThrows(RuntimeException.class, ()->productService.addProduct(addProductSchema));
    Assertions.assertEquals(0, productRepository.countAll());
  }

  @Test
  void should_not_add_product_with_name_missing() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("", 0D, "dd", 5, "d", store.getId());

    List<ProductEntity> product = productRepository.findAll();
    Assertions.assertThrows(RuntimeException.class, ()->productService.addProduct(addProductSchema));
    Assertions.assertEquals(0, productRepository.countAll());
  }

  @Test
  void should_not_add_product_with_photo_missing() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("dd", 0D, "dd", 5, "", store.getId());

    List<ProductEntity> product = productRepository.findAll();
    Assertions.assertThrows(RuntimeException.class, ()->productService.addProduct(addProductSchema));
    Assertions.assertEquals(0, productRepository.countAll());
  }

  @Test
  void should_update_product() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(
            createdPro.id(),
            "magasin",
            "rue des carriere",
            "auterive",
            "31190",
            10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto(
            "initial name",
            0D, "initial description",
            5,
            "s"
            , store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    // Update du produit
    UpdateProductDto updateProductSchema = new UpdateProductDto(
            addProduct.id(),
            "mise a jour",
            10D,
            "update description",
            15,
            "x",
            store.getId());
    ProductEntity updateProduct = productService.updateProduct(updateProductSchema);

    Assertions.assertEquals(1, productRepository.countAll());
    Assertions.assertEquals(updateProductSchema.name(), updateProduct.getName());
    Assertions.assertEquals(updateProductSchema.description(), updateProduct.getDescription());
    Assertions.assertEquals(updateProductSchema.price(), updateProduct.getPrice());
    Assertions.assertEquals(updateProductSchema.preparationTime(), updateProduct.getPreparationTime());

  }

  @Test
  void should_not_update_product_wich_not_exist() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(),
            "magasin",
            "rue des carriere",
            "auterive", "31190",
            10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto(
            "initial name",
            0D,
            "initial description",
            5,
            "s",
            store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    // Update du produit
    UpdateProductDto updateProductSchema = new UpdateProductDto(
            BigInteger.valueOf(2),
            "mise a jour",
            10D,
            "",
            15,
            "x",
            store.getId());
    Assertions.assertThrows(RuntimeException.class, ()->productService.updateProduct(updateProductSchema));
  }
  @Test
  void should_not_update_product_without_description() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("initial name", 0D, "initial description", 5, "s", store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    // Update du produit
    UpdateProductDto updateProductSchema = new UpdateProductDto(addProduct.id(), "mise a jour", 10D, "", 15, "x", store.getId());
    Assertions.assertThrows(RuntimeException.class, ()->productService.updateProduct(updateProductSchema));

    // Vérification des données du produit
    ProductEntity findProduct = productRepository.findById(addProduct.id()).orElseThrow(()->new RuntimeException(""));

    Assertions.assertEquals(1, productRepository.countAll());
    Assertions.assertEquals(addProductSchema.name(), findProduct.getName());
    Assertions.assertEquals(addProductSchema.description(), findProduct.getDescription());
    Assertions.assertEquals(addProductSchema.price(), findProduct.getPrice());
    Assertions.assertEquals(addProductSchema.preparationTime(), findProduct.getPreparationTime());
  }

  @Test
  void should_not_update_product_without_name() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("initial name", 0D, "initial description", 5, "s", store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    // Update du produit
    UpdateProductDto updateProductSchema = new UpdateProductDto(addProduct.id(), "", 10D, "desription", 15, "x", store.getId());
    Assertions.assertThrows(RuntimeException.class, ()->productService.updateProduct(updateProductSchema));

    // Vérification des données du produit
    ProductEntity findProduct = productRepository.findById(addProduct.id()).orElseThrow(()->new RuntimeException(""));

    Assertions.assertEquals(1, productRepository.countAll());
    Assertions.assertEquals(addProductSchema.name(), findProduct.getName());
    Assertions.assertEquals(addProductSchema.description(), findProduct.getDescription());
    Assertions.assertEquals(addProductSchema.price(), findProduct.getPrice());
    Assertions.assertEquals(addProductSchema.preparationTime(), findProduct.getPreparationTime());
  }

  @Test
  void should_not_update_product_without_photo() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("initial name", 0D, "initial description", 5, "s", store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    // Update du produit
    UpdateProductDto updateProductSchema = new UpdateProductDto(addProduct.id(), "mise a jour", 10D, "description", 15, "", store.getId());
    Assertions.assertThrows(RuntimeException.class, ()->productService.updateProduct(updateProductSchema));

    // Vérification des données du produit
    ProductEntity findProduct = productRepository.findById(addProduct.id()).orElseThrow(()->new RuntimeException(""));

    Assertions.assertEquals(1, productRepository.countAll());
    Assertions.assertEquals(addProductSchema.name(), findProduct.getName());
    Assertions.assertEquals(addProductSchema.description(), findProduct.getDescription());
    Assertions.assertEquals(addProductSchema.price(), findProduct.getPrice());
    Assertions.assertEquals(addProductSchema.preparationTime(), findProduct.getPreparationTime());
  }

  @Test
  void should_delete_product() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Ajout produit
    AddProductDto addProductSchema = new AddProductDto("initial name", 0D, "initial description", 5, "s", store.getId());
    AddProductResponseDto addProduct =  productService.addProduct(addProductSchema);

    // Suppression
    productService.deleteProduct(addProduct.id());

    // Recherche produit supprimé
    Assertions.assertThrows(RuntimeException.class, ()->productService.findProduct(addProduct.id()));
  }

  @Test
  void should_not_delete_if_product_not_exist() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalDto("", "password", "aaa"));

    // Creation Store
    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190", 10);
    StoreEntity store= storeService.createStore(addStoreSchema);

    // Suppression produit inexistant
    Assertions.assertThrows(RuntimeException.class, ()->productService.deleteProduct(BigInteger.valueOf(0)));
  }
}
