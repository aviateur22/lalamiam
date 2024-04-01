package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.exception.ProductException;
import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.mapper.AddProductMapper;
import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.UpdateProductDto;
import ctoutweb.lalamiam.repository.ProductRepository;
import ctoutweb.lalamiam.repository.StoreRepository;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.ProductService;
import ctoutweb.lalamiam.util.CommonFunction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


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
  public AddProductResponseDto addProduct(AddProductDto addProductSchema) {
    if(CommonFunction.isNullOrEmpty(addProductSchema.description())
            || addProductSchema.proId() == null
            || CommonFunction.isNullOrEmpty(addProductSchema.name())
            || CommonFunction.isNullOrEmpty(addProductSchema.photo())
            || addProductSchema.storeId() == null
            || addProductSchema.price().isNaN()
            || !CommonFunction.isNumber(String.valueOf(addProductSchema.preparationTime()))) throw new ProductException("Données sur le produit incorrecte", HttpStatus.BAD_REQUEST);


    // Verification existence Store
    storeRepository.findById(addProductSchema.storeId()).orElseThrow(()->new ProductException("Le store n'existe pas", HttpStatus.BAD_REQUEST));

    //Ajourt du produit
    ProductEntity product = productRepository.save(new ProductEntity(addProductSchema));
    return addProductMapper.apply(product);
  }

  @Override
  public ProductEntity updateProduct(UpdateProductDto updateProductSchema) {

    ProductEntity product = productRepository.findById(updateProductSchema.productId()).orElseThrow(()->new ProductException("le produit n'existe pas", HttpStatus.BAD_REQUEST));

    if(CommonFunction.isNullOrEmpty(updateProductSchema.description())
            ||CommonFunction.isNullOrEmpty(updateProductSchema.name())
            ||CommonFunction.isNullOrEmpty(updateProductSchema.photo())
            || updateProductSchema.storeId() == null
            || updateProductSchema.price().isNaN()
            || !CommonFunction.isNumber(String.valueOf(updateProductSchema.preparationTime()))) throw new ProductException("Données sur le produit incorrecte", HttpStatus.BAD_REQUEST);

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
  public ProductEntity findProduct(Long productId) {
    return productRepository.findById(productId).orElseThrow(()->new ProductException("Le produit n'exsite pas", HttpStatus.BAD_REQUEST));
  }

  @Override
  public void deleteProduct(Long productId) {
    findProduct(productId);
    productRepository.deleteById(productId);
  }

  @Override
  public List<ProductEntity> getStoreProducts(Long storeId) {
    return productRepository.findByStore(Factory.getStore(storeId));
  }
}
