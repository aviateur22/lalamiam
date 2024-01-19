package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.model.dto.CommandDetailDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.*;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CommandProductRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.*;
import ctoutweb.lalamiam.service.CommandService;
import ctoutweb.lalamiam.util.CommonFunction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CommandServiceImpl implements CommandService {
  private final CommandServiceHelper commandServiceHelper;
  private final StoreRepository storeRepository;
  private final CommandRepository commandRepository;
  private final CommandProductRepository cookRepository;

  public CommandServiceImpl(
          CommandServiceHelper commandServiceHelper,
          StoreRepository storeRepository,
          CommandRepository commandRepository,
          CommandProductRepository cookRepository) {
    this.commandServiceHelper = commandServiceHelper;
    this.storeRepository = storeRepository;
    this.commandRepository = commandRepository;
    this.cookRepository = cookRepository;
  }

  @Override
  public CommandDetailDto addCommand(AddCommandSchema addCommandSchema) {

    LocalDateTime now = LocalDateTime.now();
    List<BigInteger> produtcIdList = addCommandSchema
            .productsInCommand()
            .stream()
            .map(ProductWithQuantity::getProductId)
            .collect(Collectors.toList());

    String phoneClient = addCommandSchema.clientPhone();
    LocalDateTime slotTime = addCommandSchema.slotTime();

    // Vérification Store
    StoreEntity store = storeRepository
            .findById(addCommandSchema.storeId())
            .orElseThrow(()->new RuntimeException("Le commerce n'existe pas"));

    if(CommonFunction.isNullOrEmpty(phoneClient)) throw new RuntimeException("Le téléphone client est obligatoire");
    if(slotTime.isBefore(now)) throw new RuntimeException("Horaire de commande invalide");

    if(produtcIdList == null || produtcIdList.isEmpty()) throw new RuntimeException("La commande ne peut pas être vide");

    // Calcul temps de prezpartion
    return commandServiceHelper.addCommand(addCommandSchema);
  }

  @Override
  @Transactional
  public UpdateProductQuantityInCommandDto updateProductQuantityInCommand(
          UpdateProductQuantityInCommandSchema updateProductCommand
  ) throws RuntimeException {

    // Correction quantité
    if(updateProductCommand.getProductQuantity() <= 0) updateProductCommand .setProductQuantity(1);

    CommandProductEntity productCook = cookRepository.findOneProductByCommandIdProductId(
            updateProductCommand.getCommandId(),
            updateProductCommand.getProductId())
            .orElseThrow(()->new RuntimeException("Le produit n'est pas rattaché au commerce"));

    // Récuperation des données de la commande mise a jour
    CommandEntity updatedCommand = commandRepository
            .findById(updateProductCommand.getCommandId())
            .orElseThrow(()->new RuntimeException("La commande n'est pas trouvée"));

    return commandServiceHelper.updateProductQuantityInCommand(productCook, updateProductCommand, updatedCommand);
  }

  @Override
  public CommandDetailDto deleteProductInCommand(DeleteProductInCommandSchema deleteProductInCommand) {
    return commandServiceHelper.deleteProductInCommand(deleteProductInCommand);
  }

  @Override
  public CommandDetailDto addProductsInCommand(AddProductsInCommandSchema addProductsInCommand) {

    CommandEntity findCommand = commandRepository
            .findById(addProductsInCommand.commandId())
            .orElseThrow(()->new RuntimeException(""));

   return commandServiceHelper.addProductsInCommand(addProductsInCommand);
  }
}
