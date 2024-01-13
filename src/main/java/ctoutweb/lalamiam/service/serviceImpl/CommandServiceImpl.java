package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.helper.CommandServiceHelper;
import ctoutweb.lalamiam.model.schema.AddCommandSchema;
import ctoutweb.lalamiam.model.schema.ProductInCommand;
import ctoutweb.lalamiam.model.schema.UpdateProductCommandSchema;
import ctoutweb.lalamiam.repository.CommandRepository;
import ctoutweb.lalamiam.repository.CookRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.CommandEntity;
import ctoutweb.lalamiam.repository.entity.CookEntity;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.CommandService;
import ctoutweb.lalamiam.util.CommonFunction;
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
  public CommandEntity addCommand(AddCommandSchema addCommandSchema) {

    LocalDateTime now = LocalDateTime.now();
    List<BigInteger> produtcIdList = addCommandSchema
            .productsInCommand()
            .stream()
            .map(ProductInCommand::productId)
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
  public ProductInCommand updateProductCommand(UpdateProductCommandSchema updateProductCommand) {
    CookEntity productCook = cookRepository.findOneByStoreIdCommandIdProductId(
            updateProductCommand.storeId(),
            updateProductCommand.commandId(),
            updateProductCommand.productId()
    )
            .orElseThrow(()->new RuntimeException("Le produit n'est pas trouvé"));

    // Mise a jour de la quantité
    productCook.setProductQuantity(updateProductCommand.productQuantity());

    // Mise a jour des données
    CookEntity updateCookProduct = cookRepository.save(productCook);

    return new ProductInCommand(updateCookProduct.getProduct().getId(), updateProductCommand.productQuantity());
  }
}
