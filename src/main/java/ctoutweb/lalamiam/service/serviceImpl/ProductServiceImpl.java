package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.mapper.AddProductMapper;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.model.schema.UpdateProductSchema;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.ProductService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;
  private final AddProductMapper addProductMapper;

  public ProductServiceImpl(
          ProductRepository productRepository,
          StoreRepository storeRepository,
          AddProductMapper addProductMapper
  ) {
    this.productRepository = productRepository;
    this.storeRepository = storeRepository;
    this.addProductMapper = addProductMapper;
  }

  @Override
  public AddProductDto addProduct(AddProductSchema addProductSchema) {
    if(CommonFunction.isNullOrEmpty(addProductSchema.description())
            ||CommonFunction.isNullOrEmpty(addProductSchema.name())
            ||CommonFunction.isNullOrEmpty(addProductSchema.photo())
            || addProductSchema.storeId() == null
            || addProductSchema.price().isNaN()
            || !CommonFunction.isNumber(String.valueOf(addProductSchema.preparationTime()))) throw new RuntimeException("Données sur le produit incorrecte");


    // Verification existence Store
    storeRepository.findById(addProductSchema.storeId()).orElseThrow(()->new RuntimeException("Le store n'existe pas"));

    //Ajourt du produit
    ProductEntity product = productRepository.save(new ProductEntity(addProductSchema));
    return addProductMapper.apply(product);
  }

  @Override
  public ProductEntity updateProduct(UpdateProductSchema updateProductSchema) {

    ProductEntity product = productRepository.findById(updateProductSchema.productId()).orElseThrow(()->new RuntimeException("le produit n'existe pas"));

    if(CommonFunction.isNullOrEmpty(updateProductSchema.description())
            ||CommonFunction.isNullOrEmpty(updateProductSchema.name())
            ||CommonFunction.isNullOrEmpty(updateProductSchema.photo())
            || updateProductSchema.storeId() == null
            || updateProductSchema.price().isNaN()
            || !CommonFunction.isNumber(String.valueOf(updateProductSchema.preparationTime()))) throw new RuntimeException("Données sur le produit incorrecte");

    product.setName(updateProductSchema.name());
    product.setDescription(updateProductSchema.description());
    product.setPrice(updateProductSchema.price());
    product.setPreparationTime(updateProductSchema.preparationTime());
    product.setPhoto(updateProductSchema.photo());
    product.setName(updateProductSchema.name());

    ProductEntity updateProduct = productRepository.save(product);
    return updateProduct;
  }

  @Override
  public ProductEntity findProduct(BigInteger productId) {
    return productRepository.findById(productId).orElseThrow(()->new RuntimeException("Le produit n'exsite pas"));
  }

  @Override
  public void deleteProduct(BigInteger productId) {
    findProduct(productId);
    productRepository.deleteById(productId);
  }
}
