package ctoutweb.lalamiam.test;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.schema.AddStoreSchema;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.ProService;
import ctoutweb.lalamiam.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.IntrospectionException;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ProServiceTest {

  @Autowired
  ProService proService;

  @Autowired
  ProRepository proRepository;

  @Autowired
  StoreRepository storeRepository;

  @Autowired
  StoreService storeService;

  @BeforeEach
  void beforeEach() {
    proRepository.truncateAll();
  }

  @Test
  void should_not_register_pro() {
    AddProfessionalSchema addProfessionalInformation2 = new AddProfessionalSchema("aaaa", "", "aaa");
    Assertions.assertThrows(RuntimeException.class, ()->proService.addProfessional(addProfessionalInformation2));

    AddProfessionalSchema addProfessionalInformation3 = new AddProfessionalSchema("aaaa", "psps", "");
    Assertions.assertThrows(RuntimeException.class, ()->proService.addProfessional(addProfessionalInformation3));

    int proCount = proRepository.countProInDatabase();
    Assertions.assertEquals(0, proCount);

  }

  @Test
  void should_register_pro_without_phone() {
    AddProfessionalSchema addProfessionalInformation = new AddProfessionalSchema("", "password", "aaa");
    proService.addProfessional(addProfessionalInformation);
    List<ProEntity> professionalList = proRepository.findAll();
    int proCountInDatabase = proRepository.countProInDatabase();

    Assertions.assertEquals(1, proCountInDatabase);
    Assertions.assertEquals("password", professionalList.get(0).getPassword());
    Assertions.assertEquals("aaa", professionalList.get(0).getEmail());
  }

  @Test
  void should_register_pro_with_complete_information() {
    AddProfessionalSchema addProfessionalInformation1 = new AddProfessionalSchema("aaa", "aaa", "aaa");
    ProInformationDto proInformationDto = proService.addProfessional(addProfessionalInformation1);

    List<ProEntity> pro = proRepository.findAll();
    int proCountInDatabase = proRepository.countProInDatabase();
    Assertions.assertEquals(1, proCountInDatabase);
  }

  @Test
  void should_create_store() throws IntrospectionException {
    // Creation Pro
    AddProfessionalSchema addProfessionalInformation = new AddProfessionalSchema("", "password", "aaa");
    ProInformationDto createdPro = proService.addProfessional(addProfessionalInformation);

    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190");
    StoreEntity createdStore =  storeService.createStore(addStoreSchema);

    long storeCountInDatabase = storeRepository.countAll();

    Assertions.assertNotNull(createdStore);
    Assertions.assertEquals(1, storeCountInDatabase);
  }

  @Test
  void should_not_create_store_with_incomplete_info() {
    // Creation Pro
    AddProfessionalSchema addProfessionalInformation = new AddProfessionalSchema("", "password", "aaa");
    ProInformationDto createdPro = proService.addProfessional(addProfessionalInformation);

    AddStoreSchema addStoreSchema = new AddStoreSchema(createdPro.id(), "", "rue des carriere", "auterive", "31190");

    Assertions.assertThrows(RuntimeException.class, ()-> storeService.createStore(addStoreSchema));

    long storeCountInDatabase = storeRepository.countAll();
    Assertions.assertEquals(0, storeCountInDatabase);
  }

  @Test
  void should_not_create_store_with_pro_not_existing() {
    AddStoreSchema addStoreSchema = new AddStoreSchema(BigInteger.valueOf(0), "d", "rue des carriere", "auterive", "31190");
    Assertions.assertThrows(RuntimeException.class, ()-> storeService.createStore(addStoreSchema));

    long storeCountInDatabase = storeRepository.countAll();
    Assertions.assertEquals(0, storeCountInDatabase);
  }
}
