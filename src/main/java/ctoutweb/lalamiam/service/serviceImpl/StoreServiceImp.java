package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.StoreService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImp implements StoreService {

  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;

  public StoreServiceImp(StoreRepository storeRepository, ProductRepository productRepository) {
    this.storeRepository = storeRepository;
    this.productRepository = productRepository;
  }
  @Override
  public ProductEntity addProduct(AddProductSchema addProductSchema) {

    if(CommonFunction.isNullOrEmpty(addProductSchema.description())
            ||CommonFunction.isNullOrEmpty(addProductSchema.name())
            ||CommonFunction.isNullOrEmpty(addProductSchema.photo())
            || addProductSchema.store() == null
            || addProductSchema.price().isNaN()
            || !CommonFunction.isNumber(String.valueOf(addProductSchema.preparationTime()))) throw new RuntimeException("Erreur de données");


    ProductEntity product = productRepository.save(new ProductEntity(addProductSchema));
    return product;
  }

}
