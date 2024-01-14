package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.model.schema.AddProductSchema;
import ctoutweb.lalamiam.repository.entity.ProductEntity;
import ctoutweb.lalamiam.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lalamiam/v1/product")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("/")
  ResponseEntity<AddProductDto> addProduct(@RequestBody AddProductSchema addProductSchema) {
    AddProductDto addProduct = productService.addProduct(addProductSchema);
    return new ResponseEntity<AddProductDto>(addProduct,HttpStatus.OK);
  }
}
