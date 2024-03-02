package ctoutweb.lalamiam.repository.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.*;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandTransactionSession  {

  private final EntityManagerFactory entityManagerFactory;
  private final CommandProductRepository commandProductRepository;
  private final CommandRepository commandRepository;

  public CommandTransactionSession(
          CommandProductRepository commandProductRepository,
          CommandRepository commandRepository,
          EntityManagerFactory entityManagerFactory
  ) {
    this.commandProductRepository = commandProductRepository;
    this.commandRepository = commandRepository;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Transactional
  public CommandEntity updateCommand(CommandInformationToUpdate commandInformationToUpdate) {
    // Todo faire test uintaire
    CommandEntity findCommand = getCommand(commandInformationToUpdate.commandId());

    if(findCommand == null)
      throw new RuntimeException(String.format("La commande N° %s n'existe pas", findCommand.getCommandCode()));

    if(!findCommand.getStore().getId().equals(commandInformationToUpdate.storeId()))
      throw new RuntimeException(String.format("La commande N° %s n'appartient pas rattaché au commerce", findCommand.getCommandCode()));

    // Suppression des anciens produits
    commandProductRepository.deleteProductByCommandId(commandInformationToUpdate.commandId());

    // Sauvegarde des nouveaux produits
    List<CommandProductEntity> selectCommandProducts = commandInformationToUpdate.selectProducts()
      .stream()
      .map(productWithQuantity->Factory.getCommandProduct(
              commandInformationToUpdate.commandId(),
              productWithQuantity.getProductId(),
              productWithQuantity.getProductQuantity()
      )).collect(Collectors.toList());

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

  public CommandEntity saveCommand(CommandInformationToSave commandInformationToSave) {
    // Todo faire test uintaire
    CommandEntity commandToSave = Factory.getCommand(commandInformationToSave);

    // Mise a jour des info de la commande
    commandRepository.save(commandToSave);

    // Sauvegarde des nouveaux produits
    List<CommandProductEntity> selectCommandProducts = commandInformationToSave.selectProducts()
            .stream()
            .map(productWithQuantity->Factory.getCommandProduct(
                    commandToSave.getId(),
                    productWithQuantity.getProductId(),
                    productWithQuantity.getProductQuantity()
            )).collect(Collectors.toList());

    commandProductRepository.saveAllAndFlush(selectCommandProducts);
    commandToSave.setCommandProducts(selectCommandProducts);
    return commandToSave;
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
