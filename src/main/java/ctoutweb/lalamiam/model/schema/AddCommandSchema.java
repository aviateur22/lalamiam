package ctoutweb.lalamiam.model.schema;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record AddCommandSchema(
        String clientPhone,
        LocalDateTime slotTime,
        BigInteger storeId,
        List<ProductInCommand> productsInCommand) {}
