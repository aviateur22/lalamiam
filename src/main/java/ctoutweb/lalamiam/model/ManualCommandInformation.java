package ctoutweb.lalamiam.model;

import ctoutweb.lalamiam.model.dto.ProductWithQuantityDto;

import java.time.LocalDateTime;
import java.util.List;

public class ManualCommandInformation {
  private String phoneClient;
  private List<ProductWithQuantityDto> selectProducts;
  private LocalDateTime slotTime;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public ManualCommandInformation() {
  }

  public ManualCommandInformation(String phoneClient, List<ProductWithQuantityDto> selectProducts, LocalDateTime slotTime) {
    this.phoneClient = phoneClient;
    this.selectProducts = selectProducts;
    this.slotTime = slotTime;
  }

  public String getPhoneClient() {
    return phoneClient;
  }

  public void setPhoneClient(String phoneClient) {
    this.phoneClient = phoneClient;
  }

  public List<ProductWithQuantityDto> getSelectProducts() {
    return selectProducts;
  }

  public void setSelectProducts(List<ProductWithQuantityDto> selectProducts) {
    this.selectProducts = selectProducts;
  }

  public LocalDateTime getSlotTime() {
    return slotTime;
  }

  public void setSlotTime(LocalDateTime slotTime) {
    this.slotTime = slotTime;
  }
}
