package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.exception.ClientException;
import ctoutweb.lalamiam.exception.CommandException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.CommandProductListMapper;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.model.dto.ProUpdateCommandStatusDto;
import ctoutweb.lalamiam.repository.ClientCommandRepository;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CommandStatusUserRepository;
import ctoutweb.lalamiam.repository.entity.ClientCommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.CommandStatusUserEntity;
import ctoutweb.lalamiam.service.ClientService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CommandTransactionSession  {
  private static final Logger LOGGER = LogManager.getLogger();
  private final EntityManagerFactory entityManagerFactory;
  private final CommandProductRepository commandProductRepository;
  private final CommandRepository commandRepository;
  private final CommandProductListMapper commandProductListMapper;
  private final ClientCommandRepository clientCommandRepository;
  private final ClientService clientService;
  private final CommandStatusUserRepository commandStatusUserRepository;

  public CommandTransactionSession(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          EntityManagerFactory entityManagerFactory,
          CommandProductListMapper commandProductListMapper,
          ClientCommandRepository clientCommandRepository,
          ClientService clientService,
          CommandStatusUserRepository commandStatusProRepository) {
    this.commandProductRepository = commandProductRepository;
    this.commandRepository = commandRepository;
    this.entityManagerFactory = entityManagerFactory;
    this.commandProductListMapper = commandProductListMapper;
    this.clientCommandRepository = clientCommandRepository;
    this.clientService = clientService;
    this.commandStatusUserRepository = commandStatusProRepository;
  }

  @Transactional
  public CommandEntity proUpdateCommand(CommandInformationToUpdate commandInformationToUpdate) {

    CommandEntity findCommand = getCommand(commandInformationToUpdate.commandId());

    if(findCommand == null)
      throw new RuntimeException(String.format("La commande N° %s n'existe pas", commandInformationToUpdate.commandId()));

    if(!findCommand.getStore().getId().equals(commandInformationToUpdate.storeId()))
      throw new RuntimeException(String.format("La commande N° %s n'est pas rattaché au commerce", findCommand.getId()));

    // Suppression des anciens produits
    commandProductRepository.deleteProductByCommandId(commandInformationToUpdate.commandId());

    // Convertion de la liste des produits
    List<CommandProductEntity> selectCommandProducts = commandProductListMapper.apply(
            commandInformationToUpdate.selectProducts(),
            findCommand.getId());

    // Sauvegarde relation produits-commande
    commandProductRepository.saveAllAndFlush(selectCommandProducts);

    // Mise a jour des données de la cmmmande
    findCommand.setCommandProducts(selectCommandProducts);
    findCommand.setClientPhone(commandInformationToUpdate.clientPhone());
    findCommand.setSlotTime(commandInformationToUpdate.slotTime());
    findCommand.setCommandPrice(commandInformationToUpdate.commandPrice());
    findCommand.setProductQuantity(commandInformationToUpdate.numberOfProductInCommand());
    findCommand.setPreparationTime(commandInformationToUpdate.preparationTime());
    findCommand.setCommandProducts(selectCommandProducts);

    // Mise a jour des info de la commande
    commandRepository.save(findCommand);
    return findCommand;
  }

  @Transactional
  public CommandEntity clientUpdateCommand(CommandInformationToUpdate commandInformationToUpdate, Long clientId) {
    // Todo faire test
    CommandEntity findCommand = getCommand(commandInformationToUpdate.commandId());

    if(findCommand == null)
      throw new RuntimeException(String.format("La commande N° %s n'existe pas", commandInformationToUpdate.commandId()));

    if(!findCommand.getStore().getId().equals(commandInformationToUpdate.storeId()))
      throw new RuntimeException(String.format("La commande N° %s n'est pas rattaché au commerce", findCommand.getId()));

    // Suppression des anciens produits
    commandProductRepository.deleteProductByCommandId(commandInformationToUpdate.commandId());

    // Convertion de la liste des produits
    List<CommandProductEntity> selectCommandProducts = commandProductListMapper.apply(
            commandInformationToUpdate.selectProducts(),
            findCommand.getId());

    // Sauvegarde relation produits-commande
    commandProductRepository.saveAllAndFlush(selectCommandProducts);

    // Mise a jour des données de la cmmmande
    findCommand.setCommandProducts(selectCommandProducts);
    findCommand.setClientPhone(commandInformationToUpdate.clientPhone());
    findCommand.setSlotTime(commandInformationToUpdate.slotTime());
    findCommand.setCommandPrice(commandInformationToUpdate.commandPrice());
    findCommand.setProductQuantity(commandInformationToUpdate.numberOfProductInCommand());
    findCommand.setPreparationTime(commandInformationToUpdate.preparationTime());
    findCommand.setCommandProducts(selectCommandProducts);

    // Mise a jour des info de la commande
    commandRepository.save(findCommand);

    // Mise à jour de la reference client
    ClientCommandEntity clientCommand = clientCommandRepository.findOneByUserAndCommand(
            Factory.getUSer(clientId),
            findCommand)
            .orElseThrow(()->new ClientException("Impossible de mettre à jour le commande", HttpStatus.BAD_REQUEST));

    clientCommand.setUpdatedAt(LocalDateTime.now());
    clientCommandRepository.save(clientCommand);
    return findCommand;
  }
  @Transactional
  public CommandEntity proSaveCommand(CommandInformationToSave commandInformation) {
    // Todo faire test uintaire
    CommandEntity commandInformationToSave = Factory.getCommand(commandInformation);

    // Mise a jour des info de la commande
    CommandEntity commandSaved = commandRepository.save(commandInformationToSave);

    // Convertion de la liste des produits
    List<CommandProductEntity> selectCommandProducts = commandProductListMapper.apply(
            commandInformation.selectProducts(),
            commandSaved.getId());

    // Sauvegarde relation produits-commande
    commandProductRepository.saveAllAndFlush(selectCommandProducts);

    commandInformationToSave.setCommandProducts(selectCommandProducts);

    // Mise a jour du suivi de l'évolution du statut pour une initialisation de commande
    Long commandId = commandSaved.getId();
    Long proId = commandInformation.userId();
    final int INITIAL_COMMAND_STATUS_ID = 1;
    final boolean IS_PRO_ACTION = true;

    CommandStatusUserEntity commandStatusPro = Factory.getUpdateCommandStatusProFromId(
            commandId,
            INITIAL_COMMAND_STATUS_ID,
            proId, IS_PRO_ACTION);
    saveFollowedStatusAction(commandStatusPro);

    return commandInformationToSave;
  }
  @Transactional
  public CommandEntity clientSaveCommand(CommandInformationToSave commandInformation) {

    final Long clientId = commandInformation.userId();

    // Verification existance client
    if(clientService.findClient(clientId).isEmpty())
      throw new CommandException("L'identifiant client n'existe pas", HttpStatus.BAD_REQUEST);

    // Todo faire test uintaire
    CommandEntity commandInformationToSave = Factory.getCommand(commandInformation);

    // Mise a jour des info de la commande
    CommandEntity commandSaved = commandRepository.save(commandInformationToSave);

    // Sauvegrade de la reference client
    ClientCommandEntity clientCommand = new ClientCommandEntity(commandSaved, Factory.getUSer(clientId));
    clientCommandRepository.save(clientCommand);

    // Convertion de la liste des produits
    List<CommandProductEntity> selectCommandProducts = commandProductListMapper.apply(
            commandInformation.selectProducts(),
            commandSaved.getId());

    // Sauvegarde relation produits-commande
    commandProductRepository.saveAllAndFlush(selectCommandProducts);

    commandInformationToSave.setCommandProducts(selectCommandProducts);

    // Mise a jour du suivi de l'évolution du statut pour une initialisation de commande
    Long commandId = commandSaved.getId();
    final int INITIAL_COMMAND_STATUS_ID = 1;
    final boolean IS_PRO_ACTION = false;

    CommandStatusUserEntity commandStatusPro = Factory.getUpdateCommandStatusProFromId(
            commandId,
            INITIAL_COMMAND_STATUS_ID,
            clientId,
            IS_PRO_ACTION);
    saveFollowedStatusAction(commandStatusPro);

    return commandInformationToSave;
  }

  @Transactional
  public void updateCommandStatus(
          CommandEntity findCommand,
          ProUpdateCommandStatusDto proUpdateCommandStatus,
          boolean isProAction
  ) {
    Long commandId = findCommand.getId();
    int commandStatusId = proUpdateCommandStatus.commandStatus();
    Long userId = proUpdateCommandStatus.proId();

    // Mise à jour de la commande
    findCommand.setPreparedBy(Factory.getUSer(proUpdateCommandStatus.proId()));
    findCommand.setCommandStatus(Factory.getCommandStatus(proUpdateCommandStatus.commandStatus()));
    commandRepository.save(findCommand);

    // Mise à jour du suivi de modification du statuts
    CommandStatusUserEntity commandStatusPro = Factory.getUpdateCommandStatusProFromId(commandId, commandStatusId, userId, isProAction);
    saveFollowedStatusAction(commandStatusPro);
  }

  public void saveFollowedStatusAction(CommandStatusUserEntity commandStatusUser) {
    commandStatusUserRepository.save(commandStatusUser);
  }

  /**
   * Recherche des information sur une commande
   * @param commandId
   * @return
   */
  public CommandEntity getCommand(Long commandId) {
    CommandEntity command = null;
    Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
    Transaction transaction = session.beginTransaction();
    try {
      command = session.get(CommandEntity.class, commandId);

      // Si pas de commande
      if(command == null) return null;

      Hibernate.initialize(command.getCommandProducts());
      transaction.commit();
    } catch (Exception ex) {

    } finally {
      session.close();
      return command;
    }
  }


}
