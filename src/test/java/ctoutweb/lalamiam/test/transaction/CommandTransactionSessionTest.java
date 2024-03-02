package ctoutweb.lalamiam.test.transaction;

import ctoutweb.lalamiam.factory.Factory;
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
        entityManagerFactory
      )
    );

    doReturn(fakeFindRegistedCommand(commandId, storeId)).when(commandTransactionSessionSpy).getCommand(commandId);

    CommandEntity updateCommand = commandTransactionSessionSpy.updateCommand(updatedCommandInfo);

    Assertions.assertEquals(updateCommand.getId(), updatedCommandInfo.commandId());
    Assertions.assertEquals(updateCommand.getStore().getId(), updatedCommandInfo.storeId());
    Assertions.assertEquals(updateCommand.getCommandCode(), fakeFindRegistedCommand(storeId, commandId).getCommandCode());
    Assertions.assertEquals(updateCommand.getCommandProducts(),  getCommandProduct(updatedCommandInfo.selectProducts(), commandId));
    Assertions.assertEquals(updateCommand.getPreparationTime(), updatedCommandInfo.preparationTime());
    Assertions.assertEquals(updateCommand.getCommandPrice(), updatedCommandInfo.commandPrice());
    Assertions.assertEquals(updateCommand.getProductQuantity(), updatedCommandInfo.numberOfProductInCommand());

  }

  ////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Fake resulat d'une recherche de commande
   * @param commandId BigInteger
   * @param storeId BigInteger
   * @return CommandEntity
   */
  private CommandEntity fakeFindRegistedCommand(Long commandId, Long storeId) {
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
