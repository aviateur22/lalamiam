package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.model.ProductWithQuantity;
import ctoutweb.lalamiam.model.dto.*;
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
  private final CommandProductRepository commandProductRepository;

  public CommandServiceImpl(
          CommandServiceHelper commandServiceHelper,
          StoreRepository storeRepository,
          CommandRepository commandRepository,
          CommandProductRepository cookRepository) {
    this.commandServiceHelper = commandServiceHelper;
    this.storeRepository = storeRepository;
    this.commandRepository = commandRepository;
    this.commandProductRepository = cookRepository;
  }

  @Override
  public CompleteCommandDetailResponseDto addCommand(AddCommandDto addCommand) {

    LocalDateTime now = LocalDateTime.now();
    List<BigInteger> produtcIdList = addCommand
            .productsInCommand()
            .stream()
            .map(ProductWithQuantity::getProductId)
            .collect(Collectors.toList());

    String phoneClient = addCommand.clientPhone();
    LocalDateTime slotTime = addCommand.slotTime();

    // Vérification Store
    StoreEntity store = storeRepository
            .findById(addCommand.storeId())
            .orElseThrow(()->new RuntimeException("Le commerce n'existe pas"));

    if(CommonFunction.isNullOrEmpty(phoneClient)) throw new RuntimeException("Le téléphone client est obligatoire");
    if(slotTime.isBefore(now)) throw new RuntimeException("Horaire de commande invalide");

    if(produtcIdList == null || produtcIdList.isEmpty()) throw new RuntimeException("La commande ne peut pas être vide");

    // Calcul temps de prezpartion
    return commandServiceHelper.addCommand(store, addCommand);
  }

  @Override
  @Transactional
  public UpdateProductQuantityResponseDto updateProductQuantityInCommand(
          UpdateProductQuantityDto updateProductCommand
  ) throws RuntimeException {

    // Correction quantité
    if(updateProductCommand.getProductQuantity() <= 0) updateProductCommand .setProductQuantity(1);

    CommandProductEntity productCook = commandProductRepository.findOneProductByCommandIdProductId(
            updateProductCommand.getCommandId(),
            updateProductCommand.getProductId())
            .orElseThrow(()->new RuntimeException("Le produit n'est pas rattaché au commerce"));

    // Récuperation des données de la commande mise a jour
    CommandEntity updatedCommand = commandRepository
            .findById(updateProductCommand.getCommandId())
            .orElseThrow(()->new RuntimeException("La commande n'est pas trouvée"));

    return commandServiceHelper.updateProductQuantityInCommand(updateProductCommand);
  }

  @Override
  public SimplifyCommandDetailResponseDto deleteProductInCommand(DeleteProductInCommandDto deleteProductInCommand) {
    return commandServiceHelper.deleteProductInCommand(deleteProductInCommand);
  }

  @Override
  public SimplifyCommandDetailResponseDto addProductsInCommand(AddProductsInCommandDto addProductsInCommand) {

    CommandEntity findCommand = commandRepository
            .findById(addProductsInCommand.commandId())
            .orElseThrow(()->new RuntimeException(""));

   return commandServiceHelper.addProductsInCommand(addProductsInCommand);
  }
}
