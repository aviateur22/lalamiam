//package ctoutweb.lalamiam.helper;
//
//import ctoutweb.lalamiam.factory.Factory;
//import ctoutweb.lalamiam.model.dto.AddProductsInCommandDto;
//import ctoutweb.lalamiam.model.dto.AddProductsInCommandResponseDto;
//import ctoutweb.lalamiam.repository.CommandRepository;
//import ctoutweb.lalamiam.repository.ProductRepository;
//import ctoutweb.lalamiam.repository.StoreRepository;
//import ctoutweb.lalamiam.repository.entity.CommandEntity;
//import ctoutweb.lalamiam.repository.entity.StoreEntity;
//import ctoutweb.lalamiam.repository.transaction.CommandTransactionSession;
//import org.springframework.stereotype.Component;
//
//import java.math.BigInteger;
//import java.util.List;
//
//@Component
//public class CommandProductServiceHelper {
//
//  private final CommandTransactionSession commandTransaction;
//  private final ProductRepository productRepository;
//  private final CommandRepository commandRepository;
//  private final StoreRepository storeRepository;
//
//  public CommandProductServiceHelper(
//          CommandTransactionSession commandTransaction,
//          ProductRepository productRepository,
//          CommandRepository commandRepository,
//          StoreRepository storeRepository) {
//    this.commandTransaction = commandTransaction;
//    this.productRepository = productRepository;
//    this.commandRepository = commandRepository;
//    this.storeRepository = storeRepository;
//  }
//
//  /**
//   * Vérification de la commannde
//   * @param storeId BigInteger - Identifiant commerce
//   * @param commandId BigInteger - Identifiant commande
//   */
//  public void isCommandValid(BigInteger storeId, BigInteger commandId) {
//    CommandEntity updatedCommand = commandRepository
//            .findById(commandId)
//            .orElseThrow(()->new RuntimeException("La commande n'est pas trouvée"));
//  }
//
//  /**
//   * Vérification de la liste des produits a ajouter
//   * @param products List<BigInteger> - Liste des produits ajouter
//   * @param storeId BigInteger - Identitifiant Commerce
//   * @return boolean
//   */
//  public boolean areProductsValid(List<BigInteger> products, BigInteger storeId) {
//        return products
//        .stream()
//        .map(productId-> Factory.getProduct(productId)
//                .findProductById(productRepository)
//                .getStore()
//                .getId().equals(storeId))
//        .allMatch(Boolean::booleanValue) ;
//  }
//
//  /**
//   * Vérification du commerce
//   * @param storeId BigInteger - Identifiant Commerce
//   */
//  public void isStoreValid(BigInteger storeId) {
//    // Vérification du Store
//    StoreEntity store = storeRepository
//            .findById(storeId)
//            .orElseThrow(()->new RuntimeException("Le commerce n'existe pas"));
//  }
//}
