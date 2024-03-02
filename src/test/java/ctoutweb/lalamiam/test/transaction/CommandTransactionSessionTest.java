package ctoutweb.lalamiam.test.transaction;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.CommandProductListMapper;
import ctoutweb.lalamiam.model.CommandInformationToSave;
import ctoutweb.lalamiam.model.CommandInformationToUpdate;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CommandProductEntity;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class CommandTransactionSessionTest {

  CommandProductListMapper commandProductListMapper = new CommandProductListMapper();

  @Test
  void updateCommand_with_valid_update_data() {
    // Données le la commande a updater
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "06232741";
    List<ProductWithQuantity> selectProducts = Arrays.asList(
            new ProductWithQuantity(Long.valueOf(1),2),
            new ProductWithQuantity(Long.valueOf(2),2)
    );
    LocalDateTime slotTime = LocalDateTime.now();
    Integer preparationTime = 5;
    Double commandPrice = 12D;
    Integer numberOfProducts = 5;

    // Creation  CommandInformationToUpdate
    CommandInformationToUpdate updatedCommandInfo = new CommandInformationToUpdate(
            storeId,
            commandId,
            clientPhone,
            selectProducts,
            slotTime,
            preparationTime,
            numberOfProducts,
            commandPrice
    );

    // Mock entityManagerFactory
    EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
    CommandProductRepository commandProductRepository = mock(CommandProductRepository.class);
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Spy CommandTransactionSession
    CommandTransactionSession commandTransactionSessionSpy = spy( new CommandTransactionSession
      (
        commandProductRepository,
        commandRepository,
        entityManagerFactory,
        commandProductListMapper)
    );

    doReturn(fakeFindCommand(commandId, storeId)).when(commandTransactionSessionSpy).getCommand(commandId);

    CommandEntity updateCommand = commandTransactionSessionSpy.updateCommand(updatedCommandInfo);

    Assertions.assertEquals(updateCommand.getId(), updatedCommandInfo.commandId());
    Assertions.assertEquals(updateCommand.getStore().getId(), updatedCommandInfo.storeId());
    Assertions.assertEquals(updateCommand.getCommandCode(), fakeFindCommand(storeId, commandId).getCommandCode());
    Assertions.assertEquals(updateCommand.getCommandProducts(),  getCommandProduct(updatedCommandInfo.selectProducts(), commandId));
    Assertions.assertEquals(updateCommand.getPreparationTime(), updatedCommandInfo.preparationTime());
    Assertions.assertEquals(updateCommand.getCommandPrice(), updatedCommandInfo.commandPrice());
    Assertions.assertEquals(updateCommand.getProductQuantity(), updatedCommandInfo.numberOfProductInCommand());

  }
  @Test
  void updateCommand_when_command_not_exist() {
    // Données le la commande a updater
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "06232741";
    List<ProductWithQuantity> selectProducts = Arrays.asList(
            new ProductWithQuantity(Long.valueOf(1),2),
            new ProductWithQuantity(Long.valueOf(2),2)
    );
    LocalDateTime slotTime = LocalDateTime.now();
    Integer preparationTime = 5;
    Double commandPrice = 12D;
    Integer numberOfProducts = 5;

    // Creation  CommandInformationToUpdate
    CommandInformationToUpdate updatedCommandInfo = new CommandInformationToUpdate(
            storeId,
            commandId,
            clientPhone,
            selectProducts,
            slotTime,
            preparationTime,
            numberOfProducts,
            commandPrice
    );

    // Mock entityManagerFactory
    EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
    CommandProductRepository commandProductRepository = mock(CommandProductRepository.class);
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Spy CommandTransactionSession
    CommandTransactionSession commandTransactionSessionSpy = spy( new CommandTransactionSession
            (
                    commandProductRepository,
                    commandRepository,
                    entityManagerFactory,
                    commandProductListMapper)
    );

    // Renvoir une commande qui n'existe pas
    doReturn(null).when(commandTransactionSessionSpy).getCommand(commandId);

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandTransactionSessionSpy.updateCommand(updatedCommandInfo));
    Assertions.assertEquals("La commande N° 1 n'existe pas", exception.getMessage());

  }
  @Test
  void updateCommand_when_command_not_belong_to_store() {
    // Données le la commande a updater
    Long storeId = Long.valueOf(1);
    Long commandId = Long.valueOf(1);
    String clientPhone = "06232741";
    List<ProductWithQuantity> selectProducts = Arrays.asList(
            new ProductWithQuantity(Long.valueOf(1),2),
            new ProductWithQuantity(Long.valueOf(2),2)
    );
    LocalDateTime slotTime = LocalDateTime.now();
    Integer preparationTime = 5;
    Double commandPrice = 12D;
    Integer numberOfProducts = 5;

    // Creation  CommandInformationToUpdate
    CommandInformationToUpdate updatedCommandInfo = new CommandInformationToUpdate(
            storeId,
            commandId,
            clientPhone,
            selectProducts,
            slotTime,
            preparationTime,
            numberOfProducts,
            commandPrice
    );

    // Mock entityManagerFactory
    EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
    CommandProductRepository commandProductRepository = mock(CommandProductRepository.class);
    CommandRepository commandRepository = mock(CommandRepository.class);

    // Spy CommandTransactionSession
    CommandTransactionSession commandTransactionSessionSpy = spy( new CommandTransactionSession
            (
                    commandProductRepository,
                    commandRepository,
                    entityManagerFactory,
                    commandProductListMapper)
    );

    doReturn(fakeFindCommand(commandId, Long.valueOf(2))).when(commandTransactionSessionSpy).getCommand(commandId);

    Exception exception = Assertions.assertThrows(RuntimeException.class, ()->commandTransactionSessionSpy.updateCommand(updatedCommandInfo));
    Assertions.assertEquals("La commande N° 1 n'est pas rattaché au commerce", exception.getMessage());

  }
  @Test
  void saveCommand_with_valid_data() {
    // Données le la commande a enregistrer
    Long storeId = Long.valueOf(1);
    String clientPhone = "06232741";
    String commandCode = "ddddd";
    List<ProductWithQuantity> selectProducts = Arrays.asList(
            new ProductWithQuantity(Long.valueOf(1),2),
            new ProductWithQuantity(Long.valueOf(2),2),
            new ProductWithQuantity(Long.valueOf(3),1),
            new ProductWithQuantity(Long.valueOf(4),1)
    );
    LocalDateTime slotTime = LocalDateTime.now();
    Integer preparationTime = 15;
    Double commandPrice = 12D;
    Integer numberOfProducts = 6;

    // Creation  CommandInformationToUpdate
    CommandInformationToSave commandInformationToSave = new CommandInformationToSave(
            storeId,
            clientPhone,
            selectProducts,
            slotTime,
            commandCode,
            preparationTime,
            numberOfProducts,
            commandPrice
    );

    // Mock entityManagerFactory
    EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
    CommandProductRepository commandProductRepository = mock(CommandProductRepository.class);

    // Fake sauvegarde de la commande
    CommandRepository commandRepository = mock(CommandRepository.class);
    when(commandRepository.save(any(CommandEntity.class))).thenReturn(fakeSaveCommand(commandInformationToSave));

    // Spy CommandTransactionSession
    CommandTransactionSession commandTransactionSessionSpy = spy( new CommandTransactionSession(
      commandProductRepository,
      commandRepository,
      entityManagerFactory,
      commandProductListMapper));


    CommandEntity savedCommand = commandTransactionSessionSpy.saveCommand(commandInformationToSave);

    Assertions.assertNotNull(savedCommand);
    Assertions.assertEquals(15, savedCommand.getPreparationTime());
    Assertions.assertEquals("ddddd", savedCommand.getCommandCode());
    Assertions.assertEquals(6, savedCommand.getProductQuantity());
  }

  ////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Fake resulat d'une recherche de commande
   * @param commandId BigInteger
   * @param storeId BigInteger
   * @return CommandEntity
   */
  private CommandEntity fakeFindCommand(Long commandId, Long storeId) {
    CommandEntity fakeCommand = new CommandEntity();
    fakeCommand.setId(commandId);
    fakeCommand.setStore(Factory.getStore(storeId));
    fakeCommand.setClientPhone("062035");
    fakeCommand.setPreparationTime(10);
    fakeCommand.setCommandPrice(10D);
    fakeCommand.setCommandCode("dsdsd");
    fakeCommand.setCommandProducts(Arrays.asList(
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(1))),
            new CommandProductEntity(1, new ProductEntity(Long.valueOf(2)))
    ));

    return fakeCommand;
  }

  /**
   * Fake enregistrement d'une commande
   * @param  commandInformationToSave CommandInformationToSave
   * @return CommandEntity
   */
  private CommandEntity fakeSaveCommand(CommandInformationToSave commandInformationToSave) {
    CommandEntity fakeCommand = new CommandEntity();
    fakeCommand.setId(Long.valueOf(5));
    fakeCommand.setStore(Factory.getStore(commandInformationToSave.storeId()));
    fakeCommand.setClientPhone(commandInformationToSave.clientPhone());
    fakeCommand.setPreparationTime(commandInformationToSave.preparationTime());
    fakeCommand.setCommandPrice(commandInformationToSave.commandPrice());
    fakeCommand.setCommandCode(commandInformationToSave.commandCode());
    fakeCommand.setCommandProducts(commandProductListMapper.apply(commandInformationToSave.selectProducts(), fakeCommand.getId()));

    return fakeCommand;
  }

  /**
   * Transform une List<ProductWithQuantity>  en List<CommandProductEntity>
   * @param productWithQuantityList
   * @param commandId
   * @return List<CommandProductEntity>
   */
  private List<CommandProductEntity> getCommandProduct(List<ProductWithQuantity> productWithQuantityList, Long commandId) {
    return productWithQuantityList
            .stream()
            .map(productWithQuantity->Factory.getCommandProduct(
                    commandId,
                    productWithQuantity.getProductId(),
                    productWithQuantity.getProductQuantity()
            )).collect(Collectors.toList());
  }
}
