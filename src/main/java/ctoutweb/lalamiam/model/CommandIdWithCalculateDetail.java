package ctoutweb.lalamiam.model;
import java.math.BigInteger;

public record CommandIdWithCalculateDetail(BigInteger commandId, CalculateCommandDetail calculateCommandDetail) {
}
