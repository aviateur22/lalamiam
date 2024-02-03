package ctoutweb.lalamiam.test.service;

import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

  @Autowired
  CommandRepository commandRepository;

  @Autowired
  CommandProductRepository cookRepository;

  @BeforeEach
  void init() {
    commandRepository.deleteAll();
    proRepository.truncateAll();
  }



}
