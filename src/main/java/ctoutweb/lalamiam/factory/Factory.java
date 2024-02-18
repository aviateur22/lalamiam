package ctoutweb.lalamiam.factory;

import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.builder.CommandProductEntityBuilder;
import ctoutweb.lalamiam.repository.entity.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class Factory {
  public  static ProductEntity getProduct(BigInteger productId) {
    return new ProductEntity(productId);
  }
  public static CommandEntity getCommand(BigInteger commandId) { return new CommandEntity(commandId);}

  public static CommandEntity getCommand(
          CalculateCommandDetail calculateCommandDetail,
          String commandeCode,
          LocalDateTime slotTime,
          BigInteger storeId,
          String clientPhone
  ) {
    return new CommandEntity(calculateCommandDetail, commandeCode, slotTime, storeId, clientPhone);
  }

  public static StoreEntity getStore(BigInteger storeId) { return new StoreEntity(storeId);}

  public static CommandProductEntity getCommandProduct(BigInteger commandId, BigInteger productId, Integer productQuantity) {
    return CommandProductEntityBuilder.aCommandProductEntity()
            .withCommand(getCommand(commandId))
            .withProduct(getProduct(productId))
            .withProductQuantity(productQuantity)
            .build();
  }

  /***
   * Renvoie les détails complets dune commande
   * @return
   */
  public static CompleteCommandDetailResponseDto getCompleteCommandDetailDto(
          CommandEntity addCommand,
          CalculateCommandDetail calculateCommandDetail
  ) {
    return new CompleteCommandDetailResponseDto(
            addCommand.getId(),
            calculateCommandDetail.productInCommandList(),
            calculateCommandDetail.commandPreparationTime(),
            calculateCommandDetail.numberOProductInCommand(),
            calculateCommandDetail.commandPrice(),
            addCommand.getClientPhone(),
            addCommand.getCommandCode(),
            addCommand.getSlotTime()
    );
  }

  /**
   * Renvoi les détails calculés d'une commande
   * @param productsInCommand
   * @param commandPreparationTime
   * @param commandProdutcCount
   * @param commandPrice
   * @return
   */
  public static CalculateCommandDetail getCommandDetailCalculated(
          List<ProductWithQuantity> productsInCommand,
          Integer commandPreparationTime,
          Integer commandProdutcCount,
          Double commandPrice
  ) {
    return new CalculateCommandDetail(productsInCommand, commandPreparationTime, commandProdutcCount, commandPrice);

  }

  /**
   * Renvoie la mise a jour d'un produit dans une commande
   * @param commandId
   * @param product
   * @param commandDetailCalculated
   * @return
   */
  public static UpdateProductQuantityResponseDto getUpdateProductQuantityResponse(
          BigInteger commandId,
          ProductWithQuantity product,
          CalculateCommandDetail commandDetailCalculated
  ) {
    return new UpdateProductQuantityResponseDto(
            commandId,
            product,
            commandDetailCalculated.commandPreparationTime(),
            commandDetailCalculated.commandPreparationTime(),
            commandDetailCalculated.commandPrice());
  }

  /**
   * Renvoie ProdutcWithQuantity
   * @param commandProduct
   * @return
   */
  public static ProductWithQuantity getProductWithQuantity(CommandProductEntity commandProduct) {
    return new ProductWithQuantity(commandProduct.getProduct().getId(), commandProduct.getProductQuantity());
  }

  /**
   *
   * @param commandId
   * @param commandDetailCalculated
   * @return
   */
  public static SimplifyCommandDetailResponseDto getSimplifyCommandDetailResponse(
          BigInteger commandId,
          CalculateCommandDetail commandDetailCalculated
  ) {
    return new SimplifyCommandDetailResponseDto(
            commandId,
            commandDetailCalculated.productInCommandList(),
            commandDetailCalculated.commandPreparationTime(),
            commandDetailCalculated.numberOProductInCommand(),
            commandDetailCalculated.commandPrice());
  }

  /**
   * AddProductsInCommandResponseDto
   * @param addProductsInCommand Données sur l'ajout des produits
   * @param calculateCommandDetail Détail de la commande mise a jour suite à l'jout des nouveaux produits
   * @return AddProductsInCommandResponseDto
   */
  public static AddProductsInCommandResponseDto getAddProductsInCommandResponseDto(
          AddProductsInCommandDto addProductsInCommand,
          CalculateCommandDetail calculateCommandDetail
  ) {
  return new AddProductsInCommandResponseDto(
          addProductsInCommand.commandId(),
          addProductsInCommand.productWithQuantityList(),
          calculateCommandDetail.commandPreparationTime(),
          calculateCommandDetail.numberOProductInCommand(),
          calculateCommandDetail.commandPrice());
  }

  /**
   * Renvoie une commande avec ses détails de la commande
   * @param command
   * @param detail
   * @return
   */
  public static CommandWithCalculateDetail getCommandWithCalculatedDetail(CommandEntity command, CalculateCommandDetail detail) {
    return new CommandWithCalculateDetail(command, detail);
  }

  /**
   * Renvioie un produit d'une commande avec les détails de la commande(temps, prix ..)
   * @param productInCommand
   * @param detail
   * @return
   */
  public static ProductInCommandWithCalculateDetail getProductQuantityWithCalculatedDetail(CommandProductEntity productInCommand, CalculateCommandDetail detail) {
    return new ProductInCommandWithCalculateDetail(productInCommand, detail);
  }

  /**
   * Renvioie un commandeId avec les détails de la commande(temps, prix ..)
   * @param commandId
   * @param detail
   * @return
   */
  public static CommandIdWithCalculateDetail getProductCommandWithCalculatedDetail(BigInteger commandId, CalculateCommandDetail detail) {
    return new CommandIdWithCalculateDetail(commandId, detail);
  }

  /**
   * Renvoie un WeekDayEntity
   * @param date
   * @return WeekDayEntity
   */
  public static WeekDayEntity getWeekDay(LocalDateTime date) {
    int dayOfWeek = date.getDayOfWeek().getValue();
    return new WeekDayEntity(dayOfWeek);
  }

  /**
   * Renvoie un WeekDayEntity
   * @param dayOfWeek
   * @return WeekDayEntity
   */
  public static WeekDayEntity getWeekDay(Integer dayOfWeek) {
    return new WeekDayEntity(dayOfWeek);
  }
  /**
   * Horaire - magasin _jour
   * @param storeSchedule
   * @param weekDay
   * @param store
   * @return StoreWeekDayEntity
   */
  public static StoreDayScheduleEntity getStoreWeekDaySchedule(
          DailyStoreSchedule storeSchedule,
          WeekDayEntity weekDay,
          StoreEntity store
  ) {
    return new StoreDayScheduleEntity(storeSchedule, weekDay, store);
  }

  /**
   * Renvoie un DailyStoreSchedule
   * @param storeDaySchedule - StoreDayScheduleEntity
   * @return DailyStoreSchedule
   */
  public static DailyStoreSchedule getDailyStoreSchedule(StoreDayScheduleEntity storeDaySchedule) {
    return new DailyStoreSchedule(storeDaySchedule.getOpeningTime(), storeDaySchedule.getClosingTime());

  }

  /**
   * Création ProductWithQuantityDto
   * @param product ProductEntity
   * @return ProductWithQuantityDto
   */
  public static ProductWithQuantityDto getProductWithQuantityDto(ProductEntity product, Integer selectQuantity) {
    return new ProductWithQuantityDto(product.getId(), product.getName(), product.getPhoto(), product.getPrice(),selectQuantity, product.getIsAvail());
  }

  /**
   * Renvoie un  RegisterCommandeDto
   * @param command CommandEntity command - Données de la commande
   * @param products List<ProductWithQuantityDto> - Liste des produits de la commande avec les quantités assiciés
   * @return RegisterCommandDto
   */
  public static RegisterCommandDto getRegisterCommand(CommandEntity command, List<ProductWithQuantityDto> products) {

    ManualCommandInformation manualCommandInformation = new ManualCommandInformation();
    manualCommandInformation.setSelectProducts(products);
    manualCommandInformation.setPhoneClient(command.getClientPhone());
    manualCommandInformation.setSlotTime(command.getSlotTime());

    CalculatedCommandInformation calculatedCommandInformation = new CalculatedCommandInformation();
    calculatedCommandInformation.setCommandPreparationTime(command.getPreparationTime());
    calculatedCommandInformation.setCommandCode(command.getCommandCode());
    calculatedCommandInformation.setCommandePrice(command.getCommandPrice());
    calculatedCommandInformation.setProductQuantity(command.getProductQuantity());

    RegisterCommandDto registerCommand = new RegisterCommandDto();
    registerCommand.setStoreId(command.getStore().getId());
    registerCommand.setCommandId(command.getId());
    registerCommand.setManualCommandInformation(manualCommandInformation);
    registerCommand.setCalculatedCommandInformation(calculatedCommandInformation);
    return registerCommand;
  }

  /**
   * Renvoie un StoreProductsInformationDto
   * @param registerCommand RegisterCommandDto
   * @param storeProductsWithQuantity storeProductsWithQuantity
   * @return StoreProductsInformationDto
   */
  public static StoreProductsInformationDto getCommandInformationDto(RegisterCommandDto registerCommand, List<ProductWithQuantityDto> storeProductsWithQuantity) {

    // Récupération du téléphone client
    String clientPhone = registerCommand == null ? null : registerCommand.getManualCommandInformation().getPhoneClient();

    return new StoreProductsInformationDto(storeProductsWithQuantity, clientPhone);
  }

  /**
   * Renvoie un SlotAndStoreSchedule
   * @param busySlots List<LocalDateTime> - Liste des creneaux occupés
   * @param storeSchedules List<StoreDayScheduleEntity> - Horaires d'ouvertures
   * @param slotTimeAvailibilityInOneDay List<LocalDateTime> - Liste des créneaux disponible sur 24h avant filtrage
   * @return SlotAndStoreSchedule
   */
  public static SlotAndStoreSchedule getSlotAndStoreSchedule(
          List<LocalDateTime> busySlots,
          List<StoreDayScheduleEntity> storeSchedules,
          List<LocalDateTime> slotTimeAvailibilityInOneDay) {
  // Todo faire test unitaire
    return new SlotAndStoreSchedule(storeSchedules, slotTimeAvailibilityInOneDay, busySlots);

  }
}
