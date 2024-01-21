package ctoutweb.lalamiam.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ctoutweb.lalamiam.model.ProductWithQuantity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record AddCommandDto(
        String clientPhone,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime slotTime,
        BigInteger storeId,
        List<ProductWithQuantity> productsInCommand) {}
