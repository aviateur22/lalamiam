package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CookRepository;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

  @Autowired
  CommandRepository commandRepository;

  @Autowired
  CookRepository cookRepository;

  @BeforeEach
  void init() {
    commandRepository.deleteAll();
    proRepository.truncateAll();
  }



}
