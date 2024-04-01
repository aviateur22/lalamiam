package helper;

import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class ProductHelper {

  private final ProductService productService;

  public ProductHelper(ProductService productService) {
    this.productService = productService;
  }

  /**
   * Creation d'un commerce
   * @param storeId BigInteger - Identifiant Commerce
   * @return List<AddProductResponseDto>
   */
  public List<AddProductResponseDto> createProduct(Long storeId, Long proId) {
    AddProductDto addProductSchema1 = new AddProductDto(proId,"lait", 10D, "initial description", 5, "s", storeId);
    AddProductDto addProductSchema2 = new AddProductDto(proId,"coco", 20D, "initial description", 10, "s", storeId);
    AddProductDto addProductSchema3 = new AddProductDto(proId,"orange", 30D, "initial description", 20, "s", storeId);

    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);

    List<AddProductResponseDto> createdProductList = new ArrayList<>();
    createdProductList.add(addProduct1);
    createdProductList.add(addProduct2);
    createdProductList.add(addProduct3);

    return createdProductList;
  }

  /**
   * Ajout de nouveau produit apres création dela commande
   * @param storeId Long - Identifiant
   * @return List<AddProductResponseDto>
   */
  public List<AddProductResponseDto> createProductAfetrCommands(Long storeId, Long proId) {
    AddProductDto addProductSchema1 = new AddProductDto(proId,"pain", 10D, "initial description", 5, "s", storeId);
    AddProductDto addProductSchema2 = new AddProductDto(proId,"beurre", 20D, "initial description", 10, "s", storeId);
    AddProductDto addProductSchema3 = new AddProductDto(proId,"miel", 30D, "initial description", 20, "s", storeId);

    AddProductResponseDto addProduct1 =  productService.addProduct(addProductSchema1);
    AddProductResponseDto addProduct2 =  productService.addProduct(addProductSchema2);
    AddProductResponseDto addProduct3 =  productService.addProduct(addProductSchema3);

    List<AddProductResponseDto> createdProductList = new ArrayList<>();
    createdProductList.add(addProduct1);
    createdProductList.add(addProduct2);
    createdProductList.add(addProduct3);

    return createdProductList;
  }

}
