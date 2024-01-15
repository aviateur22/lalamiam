package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.model.dto.CommandDetailDto;
import ctoutweb.lalamiam.model.dto.UpdateProductQuantityInCommandDto;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.DeleteProductInCommandSchema;
import ctoutweb.lalamiam.model.schema.ProductWithQuantity;
import ctoutweb.lalamiam.model.schema.UpdateProductQuantityInCommandSchema;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CookRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CookEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
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
  private final CookRepository cookRepository;

  public CommandServiceImpl(
          CommandServiceHelper commandServiceHelper,
          StoreRepository storeRepository,
          CommandRepository commandRepository,
          CookRepository cookRepository) {
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
            .map(ProductWithQuantity::productId)
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

    CookEntity productCook = cookRepository.findOneByStoreIdCommandIdProductId(
            updateProductCommand.getStoreId(),
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
}
