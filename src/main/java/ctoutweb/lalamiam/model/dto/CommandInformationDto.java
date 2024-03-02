package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CommandInformationDto(
        Long storeId,
        Long commandId,
        LocalDate commandDate,
        LocalDateTime consultationDate,
        List<ProductWithQuantity> productsSelected) {}
