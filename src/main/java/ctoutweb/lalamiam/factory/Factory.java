package ctoutweb.lalamiam.factory;

import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.builder.CommandProductEntityBuilder;
import ctoutweb.lalamiam.repository.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Factory {
  public  static ProductEntity getProduct(Long productId) {
    return new ProductEntity(productId);
  }
  public static CommandEntity getCommand(Long commandId) { return new CommandEntity(commandId);}

  public static CommandEntity getCommand(CommandInformationToSave commandInformationToSave) {
    return new CommandEntity(commandInformationToSave);
  }

  public static StoreEntity getStore(Long storeId) { return new StoreEntity(storeId);}

  public static CommandProductEntity getCommandProduct(Long commandId, Long productId, Integer productQuantity) {
    return CommandProductEntityBuilder.aCommandProductEntity()
            .withCommand(getCommand(commandId))
            .withProduct(getProduct(productId))
            .withProductQuantity(productQuantity)
            .build();
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
   * Renvoie une CommandInformationDto
   * @param storeId
   * @param commandId
   * @param commandDate
   * @param consulationDate
   * @param selectProductsWithQuantity
   * @return CommandInformationDto
   */
  public static CommandInformationDto getCommandInformationDto(
          Long storeId,
          Long commandId,
          LocalDate commandDate,
          LocalDateTime consulationDate,
          List<ProductWithQuantity> selectProductsWithQuantity
  ) {
    return new CommandInformationDto(storeId, commandId, commandDate, consulationDate, selectProductsWithQuantity);
  }

  /**
   * Renvoie une ProductSelectInformationDto
   * @param selectProducts List<ProductWithQuantity> - Liste des produits selectionnés
   * @param clientPhone String - Tel du client
   * @return
   */
  public static ProductSelectInformationDto getProductSelectInformationDto(List<ProductWithQuantity> selectProducts, String clientPhone) {
    return new ProductSelectInformationDto(selectProducts, clientPhone);
  }

  /**
   * Renvoie un CommandInformationToUpdate
   * @Param persistInformationToUpdate PersitCommandDto
   * @param preparationtime
   * @param numberOfProductInCommand
   * @param commandPrice
   * @return CommandInformationToUpdate
   */
  public static CommandInformationToUpdate getCommandInformationToUpdate(
          PersitCommandDto persistInformationToUpdate,
          Integer preparationtime,
          Integer numberOfProductInCommand,
          Double commandPrice
          ){
    return new CommandInformationToUpdate(
            persistInformationToUpdate.storeId(),
            persistInformationToUpdate.commandId(),
            persistInformationToUpdate.clientPhone(),
            persistInformationToUpdate.selectProducts(),
            persistInformationToUpdate.selectSlotTime(),
            preparationtime,
            numberOfProductInCommand,
            commandPrice);
  }

  /**
   * Renvoie une CommandInformationToSave
   * @param commandInformationToPersist PersitCommandDto
   * @param commandCode
   * @param preparationtime
   * @param numberOfProductInCommand
   * @param commandPrice
   * @return CommandInformationToSave
   */
  public static CommandInformationToSave getCommandInformationToSave(
          PersitCommandDto commandInformationToPersist,
          String commandCode,
          Integer preparationtime,
          Integer numberOfProductInCommand,
          Double commandPrice
  ){
    return new CommandInformationToSave(
            commandInformationToPersist.storeId(),
            commandInformationToPersist.clientPhone(),
            commandInformationToPersist.selectProducts(),
            commandInformationToPersist.selectSlotTime(),
            commandCode,
            preparationtime,
            numberOfProductInCommand,
            commandPrice);
  }
}
