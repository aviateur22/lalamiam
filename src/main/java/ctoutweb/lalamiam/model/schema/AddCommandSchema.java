package ctoutweb.lalamiam.model.schema;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record AddCommandSchema(
        String clientPhone,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime slotTime,
        BigInteger storeId,
        List<ProductInCommand> productsInCommand) {}
