package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.CommandProductListMapper;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.ProductService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandTransactionSession  {
  private static final Logger LOGGER = LogManager.getLogger();
  private final EntityManagerFactory entityManagerFactory;
  private final CommandProductRepository commandProductRepository;
  private final CommandRepository commandRepository;
  private final CommandProductListMapper commandProductListMapper;

  public CommandTransactionSession(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          EntityManagerFactory entityManagerFactory,
          CommandProductListMapper commandProductListMapper) {
    this.commandProductRepository = commandProductRepository;
    this.commandRepository = commandRepository;
    this.entityManagerFactory = entityManagerFactory;
    this.commandProductListMapper = commandProductListMapper;
  }

  @Transactional
  public CommandEntity updateCommand(CommandInformationToUpdate commandInformationToUpdate) {

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
  public CommandEntity saveCommand(CommandInformationToSave commandInformation) {
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
    return commandInformationToSave;
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
