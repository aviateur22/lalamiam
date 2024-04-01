package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.AddProductResponseDto;
import ctoutweb.lalamiam.model.dto.AddProductDto;
import ctoutweb.lalamiam.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pro/product")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("")
  ResponseEntity<AddProductResponseDto> addProduct(@RequestBody AddProductDto addProductSchema) {

    AddProductResponseDto addProduct = productService.addProduct(addProductSchema);
    return new ResponseEntity<AddProductResponseDto>(addProduct,HttpStatus.OK);
  }
}
