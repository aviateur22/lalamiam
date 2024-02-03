package ctoutweb.lalamiam.test.service;

import ctoutweb.lalamiam.model.DailyStoreSchedule;
import ctoutweb.lalamiam.model.WeeklyStoreSchedule;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.model.dto.AddStoreDto;
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
import java.time.LocalTime;
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
    AddProfessionalDto addProfessionalInformation2 = new AddProfessionalDto("aaaa", "", "aaa");
    Assertions.assertThrows(RuntimeException.class, ()->proService.addProfessional(addProfessionalInformation2));

    AddProfessionalDto addProfessionalInformation3 = new AddProfessionalDto("aaaa", "psps", "");
    Assertions.assertThrows(RuntimeException.class, ()->proService.addProfessional(addProfessionalInformation3));

    int proCount = proRepository.countProInDatabase();
    Assertions.assertEquals(0, proCount);

  }

  @Test
  void should_register_pro_without_phone() {
    AddProfessionalDto addProfessionalInformation = new AddProfessionalDto("", "password", "aaa");
    proService.addProfessional(addProfessionalInformation);
    List<ProEntity> professionalList = proRepository.findAll();
    int proCountInDatabase = proRepository.countProInDatabase();

    Assertions.assertEquals(1, proCountInDatabase);
    Assertions.assertEquals("password", professionalList.get(0).getPassword());
    Assertions.assertEquals("aaa", professionalList.get(0).getEmail());
  }

  @Test
  void should_register_pro_with_complete_information() {
    AddProfessionalDto addProfessionalInformation1 = new AddProfessionalDto("aaa", "aaa", "aaa");
    ProInformationDto proInformationDto = proService.addProfessional(addProfessionalInformation1);

    List<ProEntity> pro = proRepository.findAll();
    int proCountInDatabase = proRepository.countProInDatabase();
    Assertions.assertEquals(1, proCountInDatabase);
  }

  @Test
  void should_create_store() throws IntrospectionException {
    // Creation Pro
    AddProfessionalDto addProfessionalInformation = new AddProfessionalDto("", "password", "aaa");
    ProInformationDto createdPro = proService.addProfessional(addProfessionalInformation);

    // Jours
    List<Integer> days = List.of(2, 3, 4, 5);
    List<Integer> dayMonday = List.of(1);
    List<Integer> daySunday = List.of(7);

    // Horaires
    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));

    // Horaires du commerce
    List<WeeklyStoreSchedule> storeWeekDaySchedules = List.of(
            new WeeklyStoreSchedule(days, storeScheduleMorning),
            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
    );

    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "magasin", "rue des carriere", "auterive", "31190",storeWeekDaySchedules, 10);
    StoreEntity createdStore =  storeService.createStore(addStoreSchema);

    long storeCountInDatabase = storeRepository.countAll();

    Assertions.assertNotNull(createdStore);
    Assertions.assertEquals(1, storeCountInDatabase);
  }

  @Test
  void should_not_create_store_with_incomplete_info() {
    // Creation Pro
    AddProfessionalDto addProfessionalInformation = new AddProfessionalDto("", "password", "aaa");
    ProInformationDto createdPro = proService.addProfessional(addProfessionalInformation);

    // Jours
    List<Integer> days = List.of(2, 3, 4, 5);
    List<Integer> dayMonday = List.of(1);
    List<Integer> daySunday = List.of(7);

    // Horaires
    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));

    // Horaires du commerce
    List<WeeklyStoreSchedule> storeWeekDaySchedules = List.of(
            new WeeklyStoreSchedule(days, storeScheduleMorning),
            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
    );

    AddStoreDto addStoreSchema = new AddStoreDto(createdPro.id(), "", "rue des carriere", "auterive", "31190",storeWeekDaySchedules, 10);

    Assertions.assertThrows(RuntimeException.class, ()-> storeService.createStore(addStoreSchema));

    long storeCountInDatabase = storeRepository.countAll();
    Assertions.assertEquals(0, storeCountInDatabase);
  }

  @Test
  void should_not_create_store_with_pro_not_existing() {
    // Jours
    List<Integer> days = List.of(2, 3, 4, 5);
    List<Integer> dayMonday = List.of(1);
    List<Integer> daySunday = List.of(7);

    // Horaires
    DailyStoreSchedule storeScheduleMorning = new DailyStoreSchedule(LocalTime.of(11,30,00), LocalTime.of(14,00,00));
    DailyStoreSchedule storeScheduleAfternoon = new DailyStoreSchedule(LocalTime.of(18,30,00), LocalTime.of(22,30,00));

    // Horaires du commerce
    List<WeeklyStoreSchedule> storeWeekDaySchedules = List.of(
            new WeeklyStoreSchedule(days, storeScheduleMorning),
            new WeeklyStoreSchedule(days, storeScheduleAfternoon),
            new WeeklyStoreSchedule(dayMonday, storeScheduleAfternoon),
            new WeeklyStoreSchedule(daySunday, storeScheduleAfternoon)
    );

    AddStoreDto addStoreSchema = new AddStoreDto(BigInteger.valueOf(0), "d", "rue des carriere", "auterive", "31190", storeWeekDaySchedules,10);
    Assertions.assertThrows(RuntimeException.class, ()-> storeService.createStore(addStoreSchema));

    long storeCountInDatabase = storeRepository.countAll();
    Assertions.assertEquals(0, storeCountInDatabase);
  }
}
