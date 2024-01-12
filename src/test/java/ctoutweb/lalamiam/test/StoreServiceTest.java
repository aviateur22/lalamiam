package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StoreServiceTest {

  @Autowired
  StoreService storeService;

  @Autowired
  ProService proService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProRepository proRepository;

  @BeforeEach
  void init() {
    proRepository.truncateAll();
  }

  @Test
  void should_add_product() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));

    // Creation Store
    AddStoreSchema addStoreSchema = new AddStoreSchema(new ProEntity(createdPro.id()), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity store= proService.createStore(addStoreSchema);

    // Ajout produit
    AddProductSchema addProductSchema = new AddProductSchema("melon", 1.9, "jolie melon", 5, "ddd", store);
    ProductEntity addProduct =  storeService.addProduct(addProductSchema);

    /**
     * Then
     */
    List<ProductEntity> product = productRepository.findAll();
    Assertions.assertEquals(1, productRepository.countAll());
    Assertions.assertEquals(1.9, product.get(0).getPrice());
    Assertions.assertEquals("melon", product.get(0).getName());
  }

  @Test
  void should_not_add_product_with_incomplete_info() {
    // Creation Pro
    ProInformationDto createdPro = proService.addProfessional(new AddProfessionalSchema("", "password", "aaa"));

    // Creation Store
    AddStoreSchema addStoreSchema = new AddStoreSchema(new ProEntity(createdPro.id()), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity store= proService.createStore(addStoreSchema);

    // Ajout produit
    AddProductSchema addProductSchema = new AddProductSchema("melon", 0D, "", 5, "", store);

    List<ProductEntity> product = productRepository.findAll();
    Assertions.assertThrows(RuntimeException.class, ()->storeService.addProduct(addProductSchema));
    Assertions.assertEquals(0, productRepository.countAll());

  }

  @Test
  void should_create_menu() {

  }
}
