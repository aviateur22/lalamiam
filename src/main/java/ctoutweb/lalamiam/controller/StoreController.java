package ctoutweb.lalamiam.controller;

import ctoutweb.lalamiam.model.dto.AddStoreDto;
import ctoutweb.lalamiam.repository.entity.StoreEntity;
import ctoutweb.lalamiam.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/store")
public class StoreController {

  private final StoreService storeService;

  public StoreController(StoreService storeService) {
    this.storeService = storeService;
  }

  @PostMapping("/")
  ResponseEntity<StoreEntity> addStore(@RequestBody AddStoreDto addStoreSchema) {
    StoreEntity addStore = storeService.createStore(addStoreSchema);
    return new ResponseEntity<>(addStore, HttpStatus.OK);
  }
}
