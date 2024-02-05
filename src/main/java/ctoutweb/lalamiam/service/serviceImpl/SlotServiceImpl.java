package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.helper.SlotServiceHelper;
import ctoutweb.lalamiam.model.dto.FindListOfSlotTimeAvailableDto;
import ctoutweb.lalamiam.model.dto.SelectCommandSlotDto;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.SlotService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SlotServiceImpl implements SlotService {

  private final StoreRepository storeRepository;
  private final SlotServiceHelper slotServiceHelper;

  public SlotServiceImpl(
          StoreRepository storeRepository,
          SlotServiceHelper slotServiceHelper
  ) {
    this.storeRepository = storeRepository;
    this.slotServiceHelper = slotServiceHelper;
  }

  @Override
  public List<LocalDateTime> findAllSlotAvailable(FindListOfSlotTimeAvailableDto findSlotTime) {
    // Récuperation des données du commerce
    StoreEntity store =  storeRepository.findById(findSlotTime.getStoreId()).orElseThrow();

    // Date de la commande
    final LocalDate COMMAND_DATE = findSlotTime.getCommandDate();

    // Heure début journée du jour de la commande
    final LocalDateTime START_OF_COMMAND_DAY = LocalDateTime.of(
            COMMAND_DATE.getYear(),
            COMMAND_DATE.getMonth(),
            COMMAND_DATE.getDayOfMonth(),
            0,
            0,
            0
    );

    // Heure fin de journée du jour de commande
    final LocalDateTime END_OF_COMMAND_DAY = LocalDateTime.from(START_OF_COMMAND_DAY).with(LocalTime.MAX);

    // Reférence pour filtrage des slot diponible
    final LocalDateTime REF_FILTER_TIME =
            START_OF_COMMAND_DAY.getDayOfYear() == findSlotTime.getSlotConslutationDate().getDayOfYear() ?
                    findSlotTime.getSlotConslutationDate() : START_OF_COMMAND_DAY;

    return slotServiceHelper.findListOfSlotAvailable(
                    START_OF_COMMAND_DAY,
                    END_OF_COMMAND_DAY,
                    REF_FILTER_TIME,
                    findSlotTime.getCommandPreparationTime(),
                    store
            );
  }

  @Override
  public void selectCommandSlot(SelectCommandSlotDto selectCommandSlot) {

  }

  @Override
  public void selectCommandSlot(BigInteger commandId, BigInteger storeId) {

  }
}
