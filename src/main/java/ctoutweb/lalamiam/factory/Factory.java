package ctoutweb.lalamiam.factory;

import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.CompleteCommandDetailResponseDto;
import ctoutweb.lalamiam.model.dto.SimplifyCommandDetailResponseDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityResponseDto;
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

  public static ProductWithQuantity getProductWithQuantity(BigInteger productId, Integer quantity) {
    return new ProductWithQuantity(productId, quantity);
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
}
