package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.mapper.ProductQuantityMapper;
import ctoutweb.lalamiam.model.CalculatedCommandInformation;
import ctoutweb.lalamiam.model.ManualCommandInformation;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.CommandInformationDto;
import ctoutweb.lalamiam.model.dto.*;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
import ctoutweb.lalamiam.repository.transaction.RepositoryCommonMethod;
import ctoutweb.lalamiam.service.CommandService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class CommandServiceImpl extends RepositoryCommonMethod implements CommandService {
  private final CommandServiceHelper commandServiceHelper;
  private final StoreRepository storeRepository;
  private final CommandTransactionSession commandTransactionSession;
  private final CommandRepository commandRepository;
  private final ProductQuantityMapper productQuantityMapper;
  public CommandServiceImpl (
          CommandServiceHelper commandServiceHelper,
          StoreRepository storeRepository,
          ProductRepository productRepository,
          CommandProductRepository commandProductRepository,
          CommandTransactionSession commandTransactionSession,
          CommandRepository commandRepository, ProductQuantityMapper productQuantityMapper) {
    super(commandProductRepository, productRepository);
    this.commandServiceHelper = commandServiceHelper;
    this.storeRepository = storeRepository;
    this.commandTransactionSession = commandTransactionSession;
    this.commandRepository = commandRepository;
    this.productQuantityMapper = productQuantityMapper;
  }

  @Override
  public CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommand) {
    StoreEntity store = Factory.getStore(addCommand.storeId());

    // Verification des données de la commande
    verifyRequest(addCommand);

    // Calcul temps de prezpartion
    return commandServiceHelper.addCommand(store, addCommand);
  }

  @Override
  public UpdateProductQuantityResponseDto updateProductQuantityInCommand(
          UpdateProductQuantityDto updateProductCommand
  ) throws RuntimeException {

    // Vérification que la nouvelle quantité soit positif
    if(updateProductCommand.getProductQuantity() <= 0)
      updateProductCommand.setProductQuantity(1);

    // Verification des données a modifier
    verifyRequest(
            updateProductCommand.getStoreId(),
            updateProductCommand.getCommandId(),
            Arrays.asList(updateProductCommand.getProductId())
    );

    return commandServiceHelper.updateProductQuantityInCommand(updateProductCommand);
  }

  @Override
  public SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand) {
    // Verification des données a modifier
    verifyRequest(
            deleteProductInCommand.storeId(),
            deleteProductInCommand.commandId(),
            Arrays.asList(deleteProductInCommand.productId()));

    return commandServiceHelper.deleteProductInCommand(deleteProductInCommand);
  }

  @Override
  public RegisterCommandDto getCommand(BigInteger storeId, BigInteger commandId) {

    return null;
  }

  @Override
  public CommandInformationDto createCommand(BigInteger storeId) {
    return null;
  }

  @Override
  public CommandInformationDto updateCommand(BigInteger storeId, BigInteger commandId) {
    return null;
  }

  /**
   * Vérification Commade
   */
  public void verifyRequest(BigInteger storeId, BigInteger commandId, List<BigInteger> productsId) {
    // Vérification existence du commerce
    findStore(storeId);

    // Vérification du contenu des modifications
    commandServiceHelper.verifyUpdatedCommand(commandId, productsId, storeId);
  }

  /**
   * Vérification Commade
   * @param addCommand
   */
  public void verifyRequest(AddCommandDto addCommand) {
    // Vérification existence du commerce
    findStore(addCommand.storeId());

    // Vérification du contenu de la nouvelle commande
    commandServiceHelper.verifyCreatedCommand(addCommand);
  }

  /**
   * Recherche d'un commerce
   * @param storeId
   */
  public void findStore(BigInteger storeId) {
    // Vérification du Store
    StoreEntity store = storeRepository
            .findById(storeId)
            .orElseThrow(()->new RuntimeException("Le commerce n'existe pas"));
  }

}
