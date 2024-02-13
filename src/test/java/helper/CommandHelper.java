package helper;

import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.AddCommandDto;
import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.builder.StoreEntityBuilder;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.CommandService;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHelper {

  private final ProductHelper productHelper;
  private final CommandRepository commandRepository;
  private final CommandService commandService;
  public CommandHelper(
          ProductHelper productHelper,
          CommandRepository commandRepository,
          CommandService commandService) {
    this.productHelper = productHelper;
    this.commandRepository = commandRepository;
    this.commandService = commandService;
  }

  /**
   * Génération d'une date a partir d'un jour de semaine
   * @param targetWeekDayNumber - Integer Jour de semaine à produire entre 1 et 7
   * @param dayReference - DayReference - Précise si la date doit être dans le passé future present
   * @return LocalDateTime
   */
  public LocalDateTime getDateOfDay(Integer targetWeekDayNumber, DayReference dayReference) {
    switch (dayReference) {
      case TODAY -> {
        return findNextDay(LocalDateTime.now().plusDays(1), targetWeekDayNumber);
      }
      case TOMORROW -> {
        return findNextDay(LocalDateTime.now().plusDays(2), targetWeekDayNumber);
      }
      case YESTERDAY -> {
        return findNextDay(LocalDateTime.now(), targetWeekDayNumber);
      }
      default -> throw new RuntimeException("Erreur sur la recherche d'une date");
    }
  }

  /**
   * Trouve la prochaine occurence d'une date recherchée
   * @param refDate - LocalDateTime - Date de reférence
   * @param targetWeekDayNumber - Integer jour de semaine recherché
   * @return LocalDateTime
   */
  private LocalDateTime findNextDay(LocalDateTime refDate, Integer targetWeekDayNumber) {
    return Stream.iterate(refDate, date-> date.plusDays(1))
            .limit(7)
            .filter(date-> date.getDayOfWeek().getValue() == targetWeekDayNumber)
            .findFirst()
            .orElse(null);
  }

  /**
   * Création de commande
   * @param commandDate LocalDate - date dela commande
   * @param numberOfCommands int - Nombre de commande a générer
   * @param store StoreEntity - Le commerce
   */
  public void createCommands(LocalDate commandDate, int numberOfCommands, StoreEntity store) {

    // Génreation de produits
    List<AddProductResponseDto> createProductList = productHelper.createProduct(store.getId());

    // Création d'une liste de produits
    List<ProductWithQuantity> productsInCommad = createProductsInCommand(createProductList);


    Random random = new Random();

    Stream.iterate(1, n -> n+1).limit(numberOfCommands).forEach(n->{
      int morningHour = random.nextInt(11 ,14) + 1;
      int afternoonHour = random.nextInt(18, 21) + 1;
      int minute = (random.nextInt(6)) * 10;
      int second = 0;

      LocalTime commandTimeMorning = LocalTime.of(morningHour, minute, second);
      LocalTime commandTimeAfternoon = LocalTime.of(afternoonHour, minute, second);

      LocalDateTime commandDateTimeMorning = LocalDateTime.of(
              commandDate.getYear(),
              commandDate.getMonth(),
              commandDate.getDayOfMonth(),
              commandTimeMorning.getHour(),
              commandTimeMorning.getMinute(),
              commandTimeMorning.getSecond()
      );

      LocalDateTime commandDateTimeAfternoon = LocalDateTime.of(
              commandDate.getYear(),
              commandDate.getMonth(),
              commandDate.getDayOfMonth(),
              commandTimeAfternoon.getHour(),
              commandTimeAfternoon.getMinute(),
              commandTimeAfternoon.getSecond()
      );

      if(!commandRepository.findCommandBySlotTimeAndStore(commandDateTimeMorning, store).isPresent())
        commandService.addCommand(customCommandSchema(store.getId(), commandDateTimeMorning, productsInCommad));

      if(!commandRepository.findCommandBySlotTimeAndStore(commandDateTimeAfternoon, store).isPresent())
        commandService.addCommand(customCommandSchema(store.getId(), commandDateTimeAfternoon, productsInCommad));
    });
  }

  /**
   * Liste des produits pour une commande
   * @param productList List<AddProductResponseDto> - Produit de la commande
   * @return List<ProductWithQuantity>
   */
  public List<ProductWithQuantity> createProductsInCommand(List<AddProductResponseDto> productList) {
    List<ProductWithQuantity> productsInCommand = productList
            .stream()
            .map(product -> new ProductWithQuantity(product.id(), 2))
            .collect(Collectors.toList());

    return productsInCommand;
  }

  /**
   * Creation d'un AddCommandDTO
   * @param storeId BigInteger - Identifiant
   * @param slotTime LocalDateTime - Heure dela commande
   * @param productsInCommand List<ProductWithQuantity> - Liste des prodiuts
   * @return AddCommandDto
   */
  private AddCommandDto customCommandSchema(BigInteger storeId, LocalDateTime slotTime, List<ProductWithQuantity> productsInCommand) {
    return new AddCommandDto(
            "phoneCient",
            slotTime,
            storeId,
            productsInCommand);
  }

}
