package ctoutweb.lalamiam.factory;

import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.builder.CommandProductEntityBuilder;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.repository.entity.builder.JwtUserEntityBuilder;
import ctoutweb.lalamiam.security.authentication.UserPrincipal;
import ctoutweb.lalamiam.security.authentication.UserPrincipalAuthenticationToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Factory {

  /**
   * Id par default si une action est menée par un Client
   */
  private final static long CLIENT_ACTION_ID = -1;

  public static UserEntity getUSer(Long userId) {
    // Todo faire test
    return new UserEntity(userId);
  }
  public static UserEntity getUSerWithEmailPassword(String email, String password) {
    // Todo faire test
    return new UserEntity(email, password);
  }
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
    registerCommand.setCommandStatus(command.getCommandStatus().getId());
    registerCommand.setPreparedBy(command.getPreparedBy() == null ? null : command.getPreparedBy().getName());
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
          List<ProductWithQuantity> selectProductsWithQuantity,
          LocalDateTime selectSlotTime
  ) {
    return new CommandInformationDto(storeId, commandId, commandDate, consulationDate, selectProductsWithQuantity, selectSlotTime);
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
          ProPersitCommandDto persistInformationToUpdate,
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
   * Renvoie un CommandInformationToUpdate avec un update par un client
   * @param persistInformationToUpdate
   * @param preparationtime
   * @param numberOfProductInCommand
   * @param commandPrice
   * @return CommandInformationToUpdate
   */
  public static CommandInformationToUpdate getCommandInformationToUpdate(
          ClientPersitCommandDto persistInformationToUpdate,
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
          ProPersitCommandDto commandInformationToPersist,
          String commandCode,
          Integer preparationtime,
          Integer numberOfProductInCommand,
          Double commandPrice,
          Long proId
  ){
    return new CommandInformationToSave(
            commandInformationToPersist.storeId(),
            commandInformationToPersist.clientPhone(),
            proId,
            commandInformationToPersist.selectProducts(),
            commandInformationToPersist.selectSlotTime(),
            commandCode,
            preparationtime,
            numberOfProductInCommand,
            commandPrice);
  }

  /**
   * Renvoie une CommandInformationToSave a partir d'un ClientPersitCommandDto
   * @param commandInformationToPersist
   * @param commandCode
   * @param preparationtime
   * @param numberOfProductInCommand
   * @param commandPrice
   * @return CommandInformationToSave
   */
  public static CommandInformationToSave getCommandInformationToSave(
          ClientPersitCommandDto commandInformationToPersist,
          String commandCode,
          Integer preparationtime,
          Integer numberOfProductInCommand,
          Double commandPrice
  ){
    // Todo test
    return new CommandInformationToSave(
            commandInformationToPersist.storeId(),
            commandInformationToPersist.clientPhone(),
            commandInformationToPersist.clientId(),
            commandInformationToPersist.selectProducts(),
            commandInformationToPersist.selectSlotTime(),
            commandCode,
            preparationtime,
            numberOfProductInCommand,
            commandPrice);
  }

  /**
   * Renvoie un JwtUserEntity pour recherche en base de données
   * @param user UserEntity - Utilisateur
   * @param jwtToken String - Jwt token
   * @param jwtId String - Jwt Id
   * @return JwtUserEntity
   */
  public static JwtUserEntity getJwtUserToFind(UserEntity user, String jwtToken, String jwtId) {
    // Todo Faire Test
    return JwtUserEntityBuilder.aJwtUserEntity()
            .withJwtId(jwtId)
            .withJwtToken(jwtToken)
            .withUser(user)
            .build();
  }

  /**
   * Renvoie un JwtUserEntity pour persistance en base de données
   * @param user UserEntity - Utilisateur
   * @param jwt JwtIssue - JWT surchargé des infos de création
   * @return  JwtUserEntity
   */
  public static JwtUserEntity createJwtUserToSave(UserEntity user, JwtIssue jwt) {
    // Todo Faire Test
    LocalDateTime createdAt = LocalDateTime.now();

    return JwtUserEntityBuilder.aJwtUserEntity()
            .withJwtToken(jwt.getJwtToken())
            .withJwtId(jwt.getJwtId())
            .withUser(user)
            .withIsValid(true)
            .withExpiredAt(jwt.getExpiredAt())
            .withCreatedAt(createdAt)
            .build();
  }

  /***
   * Renvoie un RoleEntity
   * @param roleId int
   * @return RoleEntity
   */
  public static RoleEntity getRoleEntity(int roleId) {
    return new RoleEntity(roleId);
  }

  /**
   * Renvoie un RoleUserEntity
   * @param roleId int
   * @param userId Long
   * @return RoleUserEntity
   */
  public static RoleUserEntity createRoleUser(int roleId, Long userId) {
    UserEntity user = Factory.getUSer(userId);
    RoleEntity role = Factory.getRoleEntity(roleId);
    return new RoleUserEntity(user, role);
  }

  /**
   * Renvoie une liste de RoleEntity à partir d'uneListe RoleUserEntity
   * @param roleUserEntities List<RoleEntity>
   * @return List<RoleEntity>
   */
  public static List<RoleEntity> getRoles(List<RoleUserEntity> roleUserEntities) {
    if(roleUserEntities.isEmpty() || roleUserEntities == null)
      return Arrays.asList();

    // Todo test
    return roleUserEntities.stream().map(roleUser-> new RoleEntity(
            roleUser.getRole().getId(),
            roleUser.getRole().getName())
    ).collect(Collectors.toList());
  }

  /**
   * Renvoie un RegisterUSerDto
   * @param user UserEntiy
   * @return RegisterUSerDto
   */
  public static RegisterUserDto createRegisterUser(UserEntity user) {
    // Todo Test
    return new RegisterUserDto(user.getId(), user.getEmail(), getRoles(user.getRoles()));
  }

  /**
   * Renvoie un UserPrincipalAuthenticationToken
   * @param user UserPrincipal
   * @return UserPrincipalAuthenticationToken
   */
  public static UserPrincipalAuthenticationToken getUserPrincipalFromUserAuthToken(UserPrincipal user) {
    // Todo test
    return new UserPrincipalAuthenticationToken(user);
  }

  /**
   * Horaire Store DTO
   * @param schedule StoreDayScheduleEntity
   * @return StoreScheduleDto
   */
  public static StoreScheduleDto createStoreDto(StoreDayScheduleEntity schedule) {
    // Todo Test
    return new StoreScheduleDto(schedule.getWeekDay().getId(), schedule.getOpeningTime(), schedule.getClosingTime());
  }

  /**
   * Renvoie un CreateStoreDto
   * @param store StoreEntity
   * @return CreateStoreDto
   */
  public static CreateStoreDto createStoreDto(StoreEntity store) {
    // Todo test
    return new CreateStoreDto(
            store.getPro().getId(),
            store.getId(),
            store.getName(),
            store.getAdress(),
            store.getCity(),
            store.getCp(),
            store.getStoreWeekDaySchedules()
                    .stream()
                    .map(schedule->createStoreDto(schedule))
                    .collect(Collectors.toList()),
            store.getFrequenceSlotTime()
    );
  }

  /**
   * Renvoie le status initial à la création d'une commande
   * @return CommandStatusEntity
   */
  public static CommandStatusEntity getInitialCommandStatus() {
    final int initialStatus = 1;
    return new CommandStatusEntity(initialStatus);
  }

  /**
   * Renvoie un status pour les commandes
   * @param status Ineteger - Status
   * @return CommandStatusEntity
   */
  public static CommandStatusEntity getCommandStatus(Integer status) {
    return new CommandStatusEntity(status);
  }

  /**
   * Renvoie un CommandStatusPro pour une initilaisation de commande
   * @param statusIdList List<Integer> - Liste des statut
   * @return List<CommandStatusEntity>
   */
  public static List<CommandStatusEntity> getCommandStatusList (List<Integer> statusIdList) {
    return statusIdList.stream()
            .map(statusId -> getCommandStatus(statusId))
            .collect(Collectors.toList());
  }

  /**
   * Renvoie un CommandStatusPro pour une initilaisation de commande
   * @param commandId Long - Identifiant commande
   * @param commandStatusId Int - Identifiant du statut
   * @param userId Long - Identitifaint utilisateur
   * @param isProAction boolean - Action menée par un pro
   * @return CommandStatusPro
   */
  public static CommandStatusUserEntity getInitialCommandStatusProFromId(Long commandId, Integer commandStatusId, Long userId, boolean isProAction) {
    return new CommandStatusUserEntity(getCommand(commandId), getCommandStatus(commandStatusId), getUSer(userId), isProAction);
  }

  /**
   * Renvoie un CommandStatusPro pour une mise a jour de statut
   * @param commandId Long - Identifiant commande
   * @param commandStatusId Int - Identifiant du statut
   * @param userId Long - Identitifaint utilisateur
   * @param isProAction boolean - Action menée par un pro
   * @return CommandStatusPro
   */
  public static CommandStatusUserEntity getUpdateCommandStatusProFromId(Long commandId, Integer commandStatusId, Long userId, boolean isProAction) {
    LocalDateTime updatedCommandStatus = LocalDateTime.now();
    return new CommandStatusUserEntity(getCommand(commandId), getCommandStatus(commandStatusId), getUSer(userId), isProAction, updatedCommandStatus);
  }

  /**
   * DashboardCommandDto à partir CommandEntity
   * @param command CommandEntity
   * @return DashboardCommandDto
   */
  public static DashboardCommandDto getDashboardCommand(CommandEntity command) {
    return new DashboardCommandDto(
            command.getId(),
            command.getCommandCode(),
            command.getProductQuantity(),
            command.getCommandPrice(),
            command.getPreparationTime(),
            command.getCommandStatus().getId(),
            command.getSlotTime()
    );
  }

  /**
   * Renvoie un DashboardDto
   * @param dashboardCommands List<DashboardCommandDto>
   * @param commandDate LocalDate
   * @return DashboardDto
   */
 public static  DashboardDto getDashboard(List<DashboardCommandDto> dashboardCommands, LocalDate commandDate) {
    return new DashboardDto(dashboardCommands, commandDate);
 }

}
